package com.zimpassflow.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.zimpassflow.databinding.ItemOnboardingBinding;
import com.zimpassflow.models.OnboardingSlide;
import java.util.List;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder> {
    private List<OnboardingSlide> slides;

    public OnboardingAdapter(List<OnboardingSlide> slides) {
        this.slides = slides;
    }

    @NonNull
    @Override
    public OnboardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OnboardingViewHolder(
                ItemOnboardingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull OnboardingViewHolder holder, int position) {
        holder.bind(slides.get(position));
    }

    @Override
    public int getItemCount() {
        return slides.size();
    }

    static class OnboardingViewHolder extends RecyclerView.ViewHolder {
        private ItemOnboardingBinding binding;

        public OnboardingViewHolder(ItemOnboardingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(OnboardingSlide slide) {
            binding.tvTitle.setText(slide.getTitle());
            binding.tvDescription.setText(slide.getDescription());
            binding.ivOnboarding.setImageResource(slide.getImageResId());
        }
    }
}