package com.zimpassflow.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayoutMediator;
import com.zimpassflow.adapters.OnboardingAdapter;
import com.zimpassflow.databinding.ActivityOnboardingBinding;
import com.zimpassflow.models.OnboardingSlide;
import com.zimpassflow.utils.PreferenceManager;
import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {
    private ActivityOnboardingBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);

        List<OnboardingSlide> slides = new ArrayList<>();
        slides.add(new OnboardingSlide("Easy Payments", "Pay your toll fees seamlessly using your mobile wallet.", android.R.drawable.ic_dialog_info));
        slides.add(new OnboardingSlide("Vehicle Management", "Register and manage multiple vehicles under one account.", android.R.drawable.ic_menu_directions));
        slides.add(new OnboardingSlide("Real-time Tracking", "Keep track of all your toll transactions and wallet balance.", android.R.drawable.ic_menu_recent_history));

        OnboardingAdapter adapter = new OnboardingAdapter(slides);
        binding.viewPager.setAdapter(adapter);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {}).attach();

        binding.btnNext.setOnClickListener(v -> {
            if (binding.viewPager.getCurrentItem() + 1 < adapter.getItemCount()) {
                binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() + 1);
            } else {
                finishOnboarding();
            }
        });

        binding.btnSkip.setOnClickListener(v -> finishOnboarding());

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == adapter.getItemCount() - 1) {
                    binding.btnNext.setText("Get Started");
                } else {
                    binding.btnNext.setText("Next");
                }
            }
        });
    }

    private void finishOnboarding() {
        preferenceManager.setOnboardingFinished(true);
        startActivity(new Intent(OnboardingActivity.this, LoginActivity.class));
        finish();
    }
}