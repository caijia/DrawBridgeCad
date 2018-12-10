package com.cj.drawbridge.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cj.drawbridge.R;
import com.cj.drawbridge.helper.GlideImageLoader;
import com.cj.drawbridge.helper.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BridgePictureListDialog extends BottomSheetDialogFragment {

    private static final int[] IMAGE_RES_ARRAY = {
            R.drawable.icon_bridge_1, R.drawable.icon_bridge_2, R.drawable.icon_bridge_3,
            R.drawable.icon_bridge_4, R.drawable.icon_bridge_5, R.drawable.icon_bridge_6,
            R.drawable.icon_bridge_7, R.drawable.icon_bridge_8, R.drawable.icon_bridge_9,
            R.drawable.icon_bridge_10, R.drawable.icon_bridge_11, R.drawable.icon_bridge_12,
            R.drawable.icon_bridge_13, R.drawable.icon_bridge_14, R.drawable.icon_bridge_15,
            R.drawable.icon_bridge_16, R.drawable.icon_bridge_17, R.drawable.icon_bridge_18,
            R.drawable.icon_bridge_13
    };

    private static final int[] IMAGE_ORDER_ARRAY = {
            18, 11, 9, 10, 12, 16, 15, 8, 7, 14, 6, 4, 13, 2, 17, 1, 3, 5, 19
    };

    private static final String[] IMAGE_TITLES = {
            "桥台、桥墩", "变截面箱梁", "变截面箱梁", "变截面箱梁", "变截面箱梁", "盖梁",
            "翼墙、耳墙", "箱梁", "T梁", "横隔板", "空心板、实心板", "空心板、实心板",
            "桥台、桥墩", "空心板、实心板", "圆桩墩", "空心板、实心板", "空心板、实心板",
            "空心板、实心板", "横隔板"
    };

    private static final int SPAN_COUNT = 3;
    private OnClickBridgeListener onClickBridgeListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_bridge_picture_list, container,
                false);
    }

    private List<BridgeImage> getData() {
        List<BridgeImage> list = new ArrayList<>();
        int length = IMAGE_RES_ARRAY.length;
        for (int i = 0; i < length; i++) {
            BridgeImage image = new BridgeImage(IMAGE_RES_ARRAY[i], IMAGE_ORDER_ARRAY[i],
                    i + 1, IMAGE_TITLES[i]);
            list.add(image);
        }
        Collections.sort(list, (o1, o2) -> o1.order - o2.order);

        //分组
        Map<String, List<BridgeImage>> map = new LinkedHashMap<>();
        for (BridgeImage bridgeImage : list) {
            String title = bridgeImage.title;
            List<BridgeImage> bridgeImages = map.get(title);
            if (bridgeImages == null) {
                bridgeImages = new ArrayList<>();
                map.put(title, bridgeImages);
            }
            bridgeImages.add(bridgeImage);
        }

        //flat
        List<BridgeImage> flatImages = new ArrayList<>();
        for (Map.Entry<String, List<BridgeImage>> entry : map.entrySet()) {
            flatImages.add(new BridgeImage(entry.getKey()));
            flatImages.addAll(entry.getValue());
        }
        return flatImages;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        List<BridgeImage> bridgeImageList = getData();
        BridgeImageAdapter adapter = new BridgeImageAdapter(bridgeImageList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), SPAN_COUNT);
        final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                BridgeImage item = adapter.getItem(i);
                if (item != null) {
                    return item.imageRes <= 0 ? SPAN_COUNT : spanSizeLookup.getSpanSize(i);
                }
                return spanSizeLookup.getSpanSize(i);
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void setOnClickBridgeListener(OnClickBridgeListener onClickBridgeListener) {
        this.onClickBridgeListener = onClickBridgeListener;
    }

    public interface OnClickBridgeListener {
        void onClickBridge(int type);
    }

    static class BridgeImage {
        public int imageRes;
        public int order;
        public int bridgeType;
        public String title;
        public int viewType;

        public BridgeImage(int imageRes, int order, int bridgeType, String title) {
            this.imageRes = imageRes;
            this.order = order;
            this.bridgeType = bridgeType;
            this.title = title;
        }

        public BridgeImage(String title) {
            this.title = title;
        }
    }

    class BridgeImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE_TITLE = 1;
        private static final int TYPE_PHOTO = 2;
        private List<BridgeImage> bridgeImages;

        public BridgeImageAdapter(List<BridgeImage> bridgeImages) {
            this.bridgeImages = bridgeImages;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            if (viewType == TYPE_PHOTO) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_bridge_picture, viewGroup, false);
                return new BridgeImageVH(view);

            } else {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_bridge_title, viewGroup, false);
                return new BridgeTitleVH(view);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
            BridgeImage bridgeImage = getItem(position);
            int viewType = bridgeImage.viewType;
            if (viewType == TYPE_TITLE) {
                BridgeTitleVH holder = (BridgeTitleVH) viewHolder;
                holder.tvTitle.setText(bridgeImage.title);

            } else {
                BridgeImageVH holder = (BridgeImageVH) viewHolder;
                ViewGroup.LayoutParams params = holder.flImageContainer.getLayoutParams();
                params.width = params.height = Util.getScreenWidth(getContext()) / SPAN_COUNT;

                GlideImageLoader.getInstance().loadImage(holder.ivBridge, bridgeImage.imageRes,
                        R.drawable.shape_black, params.width, params.height);

                holder.setExtra(bridgeImage);
                holder.vClick.setOnClickListener(holder);
            }
        }

        @Override
        public int getItemViewType(int position) {
            BridgeImage bridgeImage = getItem(position);
            bridgeImage.viewType = bridgeImage.imageRes <= 0 ? TYPE_TITLE : TYPE_PHOTO;
            if (bridgeImage.imageRes <= 0) {
                return TYPE_TITLE;
            } else {
                return TYPE_PHOTO;
            }
        }

        public BridgeImage getItem(int position) {
            if (bridgeImages == null || position < 0 || position >= bridgeImages.size()) {
                return null;
            }
            return bridgeImages.get(position);
        }

        @Override
        public int getItemCount() {
            return bridgeImages.size();
        }

        class BridgeImageVH extends RecyclerView.ViewHolder implements View.OnClickListener {
            private ImageView ivBridge;
            private BridgeImage bridgeImage;
            private FrameLayout flImageContainer;
            private View vClick;

            public BridgeImageVH(@NonNull View itemView) {
                super(itemView);
                ivBridge = itemView.findViewById(R.id.iv_bridge);
                flImageContainer = itemView.findViewById(R.id.fl_image_container);
                vClick = itemView.findViewById(R.id.v_click);
            }

            @Override
            public void onClick(View v) {
                int type = bridgeImage.bridgeType;
                if (onClickBridgeListener != null) {
                    onClickBridgeListener.onClickBridge(type);
                }
            }

            public void setExtra(BridgeImage bridgeImage) {
                this.bridgeImage = bridgeImage;
            }
        }

        class BridgeTitleVH extends RecyclerView.ViewHolder {
            TextView tvTitle;

            public BridgeTitleVH(@NonNull View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tv_title);
            }
        }
    }
}
