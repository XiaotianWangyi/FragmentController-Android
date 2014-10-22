
package com.xmht.lockscreen.core;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class FragmentController {

    public static final int FLAG_NORMAL = 10000;
    public static final int FLAG_SINGLE_TASK = 10001;
    public static final int FLAG_SINGLE_TOP = 10002;

    private List<Fragment> stack = new ArrayList<Fragment>();
    private int containerId;
    private FragmentActivity homeActivity;
    private FragmentManager fragmentManager;
    private AnimationContainer animationContainer;
    private FragmentTransaction transaction;

    public FragmentController(FragmentActivity homeActivity, int containerId,
            AnimationContainer animationContainer) {
        this.containerId = containerId;
        this.homeActivity = homeActivity;
        this.animationContainer = animationContainer;
        init();
    }

    private void init() {
        fragmentManager = homeActivity.getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
    }

    public void changeFragment(Fragment fragment) {
        changeFragment(fragment, FLAG_NORMAL);
    }

    public void changeFragment(Fragment fragment, int flag) {
        addToStack(fragment, flag);
        changeFragment(fragment, false, false);
    }

    public void changeFragment(Fragment fragment, Bundle bundle) {
        fragment.setArguments(bundle);
        changeFragment(fragment);
    }

    public void changeFragment(Fragment fragment, Bundle bundle, int flag) {
        fragment.setArguments(bundle);
        changeFragment(fragment, flag);
    }

    private void changeFragment(Fragment fragment, boolean onBack, boolean onRemove) {
        if (onRemove) {
            if (fragment.isAdded()) {
                if (animationContainer != null) {
                    transaction.setCustomAnimations(animationContainer.exit,
                            animationContainer.exit);
                }
                transaction.remove(fragment).commit();
            }
            return;
        }
        if (animationContainer != null) {
            if (onBack) {
                transaction.setCustomAnimations(animationContainer.exit, animationContainer.exit);
            } else {
                transaction.setCustomAnimations(animationContainer.enter, animationContainer.enter);
            }
        }
        transaction.add(containerId, fragment).addToBackStack(null).commit();
    }

    private void addToStack(Fragment fragment, int flag) {
        if (FLAG_SINGLE_TASK == flag) {
            stack.removeAll(stack);
        } else if (FLAG_SINGLE_TOP == flag) {
            int size = stack.size();
            for (int i = 0; i < size; i++) {
                if (stack.get(i).getClass().getSimpleName()
                        .equals(fragment.getClass().getSimpleName())) {
                    stack.removeAll(stack.subList(i, size - 1));
                    break;
                }
            }
        }
        stack.add(fragment);
    }

    public void backPressed() {
        int size = stack.size();
        changeFragment(stack.remove(size - 1), true, true);
        if (size == 1) {
            homeActivity.finish();
        }
    }

    public static class AnimationContainer {
        public int enter, exit;

        public AnimationContainer(int enter, int exit) {
            this.enter = enter;
            this.exit = exit;
        }
    }
}
