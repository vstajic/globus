package com.busglo.globus.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.busglo.globus.MainActivity;
import com.busglo.globus.R;
import com.busglo.globus.domain.user.User;
import com.busglo.globus.domain.user.UsersAdapter;
import com.busglo.globus.rest.IRewardGatewayApi;
import com.busglo.globus.rest.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class HomeFragment extends Fragment {

    private RecyclerView mUserRecyclerView;
    private List<User> users = new ArrayList<>();
    private IRewardGatewayApi rewardGatewayApi;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private FloatingActionButton floatingActionButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        floatingActionButton = view.findViewById(R.id.syncUserListFab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchUsers();
            }
        });

        mUserRecyclerView = view.findViewById(R.id.userList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mUserRecyclerView.setLayoutManager(mLayoutManager);
        mUserRecyclerView.setItemAnimator(new DefaultItemAnimator());

        Retrofit retrofit = RetrofitClient.getInstance();
        rewardGatewayApi = retrofit.create(IRewardGatewayApi.class);

        fetchUsers();

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        compositeDisposable.clear();
    }

    private void fetchUsers() {
        if (isNetworkAvailable()) {
            fetchUsersUsingRestService();
        } else {
            fetchUsersUsingDatabase();
        }
    }

    private void updateDatabase(final List<User> users) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                MainActivity.globusDatabase.userDao().insertUser(users.toArray(new User[users.size()]));

            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d("room", "onSubscribe");
            }

            @Override
            public void onComplete() {
                Log.d("room", "onUserAdded");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("room", "onDataNotAvailable");
            }
        });
    }

    private void fetchUsersUsingRestService() {
        String medium = "medium";
        String base = medium + ":" + medium;
        String authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
        compositeDisposable.add(rewardGatewayApi
                .fetchUsers(authHeader)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<User>>() {
                    @Override
                    public void accept(List<User> users) throws Exception {
                        displayUsers(users);
                        updateDatabase(users);
                    }
                }));
        updateDatabase(users);
    }

    private void fetchUsersUsingDatabase() {
        compositeDisposable.add(
                MainActivity.globusDatabase
                        .userDao()
                        .findAllUsers()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<User>>() {
                            @Override
                            public void accept(@io.reactivex.annotations.NonNull List<User> users) throws Exception {
                                displayUsers(users);
                            }
                        }));
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void displayUsers(List<User> users) {
        RecyclerView.Adapter adapter = new UsersAdapter(getActivity(), users);
        mUserRecyclerView.setAdapter(adapter);
    }

}

