package com.example.catl.hulibarry.interface_ulite;

/**
 * Created by HSL on 2020/12/23.
 */

public interface IActivityExtend {
    void setDiaLogType(int diaLogType);

    void hideLoadingIndicatorDialog();

    void showLoadingIndicatorDialog();

    void showLoadingIndicatorDialog(String message);

    void showLoadingIndicatorDialog(int time);

    void showLoadingIndicatorDialog(String message, int time);
}
