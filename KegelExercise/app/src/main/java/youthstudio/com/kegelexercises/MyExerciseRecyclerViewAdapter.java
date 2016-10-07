package youthstudio.com.kegelexercises;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;


public class MyExerciseRecyclerViewAdapter extends RecyclerView.Adapter<MyExerciseRecyclerViewAdapter.ViewHolder> {

    private final List<ExerciseItem> mValues;
    private final ListExerciseFragment.OnListFragmentInteractionListener mListener;
    private Context context;

    public MyExerciseRecyclerViewAdapter(List<ExerciseItem> items,
                                         ListExerciseFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.exercise_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
//        holder.mImgExerciseImage.setImageURI();
        holder.mTvwExerciseName.setText(mValues.get(position).instructionName);
        //asset file
        try {
            GifDrawable gifFromAssets = new GifDrawable( context.getAssets(), holder.mItem.imageName);
            holder.mGifExerciseImage.setImageDrawable(gifFromAssets);
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
//        public final ImageView mImgExerciseImage;
        public final TextView mTvwExerciseName;
        public GifImageView mGifExerciseImage;
        public ExerciseItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
//            mImgExerciseImage = (ImageView) view.findViewById(R.id.exercise_image);
            mTvwExerciseName = (TextView) view.findViewById(R.id.exercise_name);
            mGifExerciseImage = (GifImageView) view.findViewById(R.id.exercise_gif);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTvwExerciseName.getText() + "'";
        }
    }
}
