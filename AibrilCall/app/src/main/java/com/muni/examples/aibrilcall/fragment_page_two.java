package com.muni.examples.aibrilcall;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link fragment_page_two.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link fragment_page_two#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_page_two extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ListView listview ;
    private KeywordListViewAdapter adapter;


    public fragment_page_two() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_page_two.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_page_two newInstance(String param1, String param2) {
        fragment_page_two fragment = new fragment_page_two();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View view = inflater.inflate(R.layout.activity_keyword_list, container, false);

        // Adapter 생성
        adapter = new KeywordListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) view.findViewById(R.id.listview1);
        listview.setAdapter(adapter);

        // 첫 번째 아이템 추가.
        adapter.addItem("1", "키워드1") ;
        // 두 번째 아이템 추가.
        adapter.addItem("2", "키워드2") ;
        // 세 번째 아이템 추가.
        adapter.addItem("3", "키워드3") ;
        // 첫 번째 아이템 추가.
        adapter.addItem("4", "키워드4") ;
        // 두 번째 아이템 추가.
        adapter.addItem("5", "키워드5") ;
        // 세 번째 아이템 추가.
        adapter.addItem("6", "키워드6") ;
        // 첫 번째 아이템 추가.
        adapter.addItem("7", "키워드7") ;
        // 두 번째 아이템 추가.
        adapter.addItem("8", "키워드8") ;
        // 세 번째 아이템 추가.
        adapter.addItem("9", "키워드9") ;
        // 첫 번째 아이템 추가.
        adapter.addItem("10", "키워드10") ;



        // 위에서 생성한 listview에 클릭 이벤트 핸들러 정의.
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                KeywordViewItem item = (KeywordViewItem) parent.getItemAtPosition(position) ;

                String numStr = item.getNumStr();
                String keywordStr = item.getKeywordStr();

                // TODO : use item data.
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
/*        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
