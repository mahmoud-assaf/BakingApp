package com.mahmoud.bakingapp.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mahmoud.bakingapp.MainActivity;
import com.mahmoud.bakingapp.R;
import com.mahmoud.bakingapp.adapter.StepsListAdapter;
import com.mahmoud.bakingapp.model.Recipe;
import com.mahmoud.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StepsFragment.OnStepsFragmentListener} interface
 * to handle interaction events.
 * Use the {@link StepsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepsFragment extends Fragment implements StepsListAdapter.ItemClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_RECIPE = "recipe";
    private static final String ARG_POSITION = "position";
    private static final String LAYOUT_STATE = "layout_state";


    // TODO: Rename and change types of parameters
    private Recipe recipe;
    private RecyclerView mListView;
    List<Step> steps=new ArrayList<>();
    private OnStepsFragmentListener mListener;
    public  StepsListAdapter adapter;
    public int step_position;
   public LinearLayoutManager linearLayoutManagerm;
    public Parcelable mLayoutManagerState;
    private static final String LAYOUT_MANAGER_STATE = "LAYOUT_MANAGER_STATE";
    public StepsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.

     * @return A new instance of fragment StepsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StepsFragment newInstance(Recipe param1,int param2) {
        StepsFragment fragment = new StepsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_RECIPE, param1);
        args.putInt(ARG_POSITION,param2);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            recipe = getArguments().getParcelable(ARG_RECIPE);
            step_position=getArguments().getInt(ARG_POSITION);
            steps=recipe.steps;
            adapter=new StepsListAdapter(getActivity(),steps,step_position);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Log.e("steps fragment", "on createview");
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_steps, container, false);
        mListView=view.findViewById(R.id.steps_list_view);

        mListView.setAdapter(adapter);

        adapter.setClickListener(StepsFragment.this);
        linearLayoutManagerm=new LinearLayoutManager(getActivity());
        mListView.setLayoutManager(linearLayoutManagerm);
        if (savedInstanceState != null) {
            //Log.e("steps fragment", "on createview saved instance");
            mLayoutManagerState = savedInstanceState.getParcelable(LAYOUT_MANAGER_STATE);
            mListView.getLayoutManager().onRestoreInstanceState(mLayoutManagerState);
        }
       // mListView.getLayoutManager().scrollToPosition(step_position);
       // Log.e("step num inside steps", String.valueOf(step_position));
        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStepsFragmentListener) {
            mListener = (OnStepsFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        mLayoutManagerState = mListView.getLayoutManager().onSaveInstanceState();
       // Log.e("steps fragment", "on pause");
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Log.e("steps fragment", "on saveinstancestate()");
        outState.putParcelable(LAYOUT_MANAGER_STATE, mLayoutManagerState);
    }

    @Override
    public void onItemClick(View view, int position) {
        mListener.onStepSelected(position);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnStepsFragmentListener {
        // TODO: Update argument type and name
        void onStepSelected(int position);
    }
}
