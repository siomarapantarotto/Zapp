package br.com.siomara.zapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import br.com.siomara.zapp.fragment.ChatsFragment;
import br.com.siomara.zapp.fragment.ContactsFragment;

/**
 * Created by 80114369 on 17/04/2018.
 */

public class TabAdapter extends FragmentStatePagerAdapter {

    private String[] tabTitles = {"CHATS", "CONTACTS"};

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    // Retorna fragmento desejado para ser carregado
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position) {
            case  0 :
                fragment = new ChatsFragment();
                break;
            case 1 :
                fragment = new ContactsFragment();
                break;
        }
        return fragment;
    }

    @Override
    // Retorna tamanho do array que indica quantidade de abas
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

}
