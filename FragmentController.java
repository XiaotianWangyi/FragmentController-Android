
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
    private AnimationContainer tempContainer;

    public FragmentController(FragmentActivity homeActivity, int containerId,
            AnimationContainer animationContainer) {
        this.containerId = containerId;
        this.homeActivity = homeActivity;
        this.animationContainer = animationContainer;
        init();
    }

    private void init() {
        fragmentManager = homeActivity.getSupportFragmentManager();
    }

    public void changeFragment(Fragment fragment) {
        changeFragment(fragment, FLAG_NORMAL);
    }

    public void changeFragment(Fragment fragment, int flag) {
        addToStack(fragment, flag);
        replaceFragment(fragment);
    }

    public void changeFragment(Fragment fragment, Bundle bundle) {
        fragment.setArguments(bundle);
        changeFragment(fragment);
    }

    public void changeFragment(Fragment fragment, Bundle bundle, int flag) {
        fragment.setArguments(bundle);
        changeFragment(fragment, flag);
    }

    public void addTempContainer(AnimationContainer tempContainer) {
        this.tempContainer = tempContainer;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (tempContainer != null) {
            transaction.setCustomAnimations(tempContainer.enter, tempContainer.exit,
                    tempContainer.popEnter, tempContainer.popExit);
            tempContainer = null;
        } else if (animationContainer != null) {
            transaction.setCustomAnimations(animationContainer.enter, animationContainer.exit,
                    animationContainer.popEnter, animationContainer.popExit);
        }
        transaction.replace(containerId, fragment);
        transaction.commit();
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
        if (size == 1) {
            homeActivity.finish();
        }
        stack.remove(size - 1);
        Fragment fragment = stack.get(size - 2);
        replaceFragment(fragment);
    }

    public static class AnimationContainer {
        public int enter, exit, popEnter, popExit;

        public AnimationContainer(int enter, int exit, int popEnter, int popExit) {
            this.enter = enter;
            this.exit = exit;
            this.popEnter = popEnter;
            this.popExit = popExit;
        }
    }
}
