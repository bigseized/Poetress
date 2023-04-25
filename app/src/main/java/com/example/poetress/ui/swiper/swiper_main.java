package com.example.poetress.ui.swiper;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.poetress.databinding.FragmentSwiperMainBinding;
import com.example.poetress.ui.swiper.cardstack.CardStackAdapter;
import com.example.poetress.view_model.SwiperMainViewModel;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackView;

public class swiper_main extends Fragment {

    private SwiperMainViewModel mViewModel;
    private CardStackLayoutManager manager;
    private CardStackAdapter adapter;
    private CardStackView cardStackView;
    FragmentSwiperMainBinding binding;

    public static swiper_main newInstance() {
        return new swiper_main();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

//    private List<CardStackItem> addList() { //Меняем ArrayList на нужную Collection из Firestore
//        List<CardStackItem> items = new ArrayList<>();
//        items.add(new CardStackItem("Test1","Something..."));
//        items.add(new CardStackItem("Test2","Something..."));
//        items.add(new CardStackItem("Test3","Something..."));
//        items.add(new CardStackItem("Test4","Something..."));
//        items.add(new CardStackItem("Test5","Something..."));
//        return items;
//    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSwiperMainBinding.inflate(inflater, container, false);
//        cardStackView = binding.cardStackHolder;
//        adapter = new CardStackAdapter(addList());
//        manager = new CardStackLayoutManager(getActivity().getApplicationContext(), new CardStackListener() {
//            @Override
//            public void onCardDragging(Direction direction, float ratio) {
//
//            }
//
//            @Override
//            public void onCardSwiped(Direction direction) { //обрабатываем свайпы в разные стороны
//                switch (direction){
//                    case Top:
//                        Toast.makeText(getActivity(),"Direction: Top", Toast.LENGTH_SHORT);
//                        break;
//                    case Right:
//                        Toast.makeText(getActivity(),"Direction: Right", Toast.LENGTH_SHORT);
//                        break;
//                    case Left:
//                        Toast.makeText(getActivity(),"Direction: Left", Toast.LENGTH_SHORT);
//                        break;
//                    case Bottom:
//                        break;
//                }
//            }
//
//            @Override
//            public void onCardRewound() {
//
//            }
//
//            @Override
//            public void onCardCanceled() {
//
//            }
//
//            @Override
//            public void onCardAppeared(View view, int position) {
//
//
//            }
//
//            @Override
//            public void onCardDisappeared(View view, int position) {
//
//            }
//        });
//        manager.setStackFrom(StackFrom.None);
//        manager.setVisibleCount(3);
//        manager.setTranslationInterval(8.0f);
//        manager.setScaleInterval(0.95f);
//        manager.setSwipeThreshold(0.3f);
//        manager.setMaxDegree(20.0f);
//        manager.setDirections(Direction.FREEDOM);
//        manager.setCanScrollHorizontal(true);
//        manager.setSwipeableMethod(SwipeableMethod.Manual);
//        manager.setOverlayInterpolator(new LinearInterpolator());
//        cardStackView.setLayoutManager(manager);
//        cardStackView.setAdapter(adapter);
//        cardStackView.setItemAnimator(new DefaultItemAnimator());
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SwiperMainViewModel.class);
        // TODO: Use the ViewModel
    }

}
