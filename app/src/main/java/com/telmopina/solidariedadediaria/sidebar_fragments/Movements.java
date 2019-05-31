package com.telmopina.solidariedadediaria.sidebar_fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.telmopina.solidariedadediaria.R;
import com.telmopina.solidariedadediaria.base.BaseFragment;
import com.telmopina.solidariedadediaria.dao.MovementResponse;
import com.telmopina.solidariedadediaria.utils.AppGlobals;
import com.telmopina.solidariedadediaria.webservice.ApiCallbacks;
import com.telmopina.solidariedadediaria.webservice.ApiClient;
import com.telmopina.solidariedadediaria.webservice.ApiInterface;
import com.telmopina.solidariedadediaria.webservice.WebApiConstants;
import com.telmopina.solidariedadediaria.webservice.WebServiceCaller;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class Movements extends BaseFragment implements ApiCallbacks {

    private View mBaseView;
    private RecyclerView recyclerView;
    private ListAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.movements));
        mBaseView = inflater.inflate(R.layout.fragment_movements, container, false);
        recyclerView = mBaseView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getAllRecords();

        return mBaseView;
    }

    private void getAllRecords() {
        // api call to retrieve all movements/transactions of a user
        mActivity.showProgress();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id", Integer.parseInt(AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_ID)));
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> call = apiService.getAllMovements(jsonObject);
        WebServiceCaller.CallWebApi(call, WebApiConstants.GET_ALL_MOVEMENTS, mActivity, this);
    }

    @Override
    public void onSuccess(JsonObject jsonObject, Enum anEnum) {
        mActivity.hideProgress();
        if (anEnum == WebApiConstants.GET_ALL_MOVEMENTS) {
            // on api response, convert data to model and pass to list adapter
            Type type = new TypeToken<ArrayList<MovementResponse>>() {
            }.getType();
            ArrayList<MovementResponse> response = (ArrayList<MovementResponse>) new Gson().fromJson(WebServiceCaller.getResponsePacketArray(jsonObject), type);
            adapter = new ListAdapter(response);
            recyclerView.setAdapter(adapter);
            System.out.println(response);
        }
    }

    @Override
    public void onError(JsonObject jsonObject, Enum anEnum) {
        mActivity.hideProgress();
        mActivity.showToast(WebServiceCaller.getResponseMessage(jsonObject));
    }

    public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
        private List<MovementResponse> arrayList;

        private ListAdapter(ArrayList<MovementResponse> arrayList) {
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delegate_movement, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ListAdapter.ViewHolder holder, final int position) {
            System.out.println(arrayList.get(position).getInstitute());
            holder.instituteName.setText(arrayList.get(position).getInstitute());
            holder.money.setText(String.valueOf(arrayList.get(position).getMoney()));

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView instituteName;
            TextView money;

            ViewHolder(View view) {
                super(view);
                instituteName = view.findViewById(R.id.institute_name);
                money = view.findViewById(R.id.institute_money);
            }
        }
    }
}
