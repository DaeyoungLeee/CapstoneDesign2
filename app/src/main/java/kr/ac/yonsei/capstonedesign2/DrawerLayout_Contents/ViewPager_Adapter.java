package kr.ac.yonsei.capstonedesign2.DrawerLayout_Contents;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public class ViewPager_Adapter extends FragmentStatePagerAdapter {

    List<Fragment> fragmentList;

    public ViewPager_Adapter(@NonNull FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.fragmentList = list;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
