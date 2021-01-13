package com.fastaccess.ui.widgets.dialog;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;

import com.fastaccess.R;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.Bundler;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.ui.base.BaseBottomSheetDialog;
import com.fastaccess.ui.widgets.FontButton;
import com.fastaccess.ui.widgets.FontTextView;
import com.prettifier.pretty.PrettifyWebView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Kosh on 16 Sep 2016, 2:15 PM
 */

public class MessageDialogView extends BaseBottomSheetDialog {

    public static final String TAG = MessageDialogView.class.getSimpleName();

    public interface MessageDialogViewActionCallback {

        void onMessageDialogActionClicked(boolean isOk, @Nullable Bundle bundle);

        void onDialogDismissed();
    }

    public @interface ButtonEnum {
        int POSITIVE = 0;
        int NEGATIVE = 1;
        int NEUTRAL = 2;
    }

    public interface MessageDialogViewButtonCallback {

        void onMessageDialogButtonClicked(@ButtonEnum int actionType, @Nullable Bundle bundle);

    }

    @BindView(R.id.prettifyWebView) PrettifyWebView prettifyWebView;
    @BindView(R.id.title) FontTextView title;
    @BindView(R.id.message) FontTextView message;
    @BindView(R.id.cancel) FontButton cancel;
    @BindView(R.id.ok) FontButton ok;
    @BindView(R.id.neutral) FontButton neutral;

    @Nullable private MessageDialogViewActionCallback callback;
    @Nullable private MessageDialogViewButtonCallback buttonCallback;

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        if (getParentFragment() != null && getParentFragment() instanceof MessageDialogViewActionCallback) {
            callback = (MessageDialogViewActionCallback) getParentFragment();
        } else if (context instanceof MessageDialogViewActionCallback) {
            callback = (MessageDialogViewActionCallback) context;
        }

        if (getParentFragment() != null && getParentFragment() instanceof MessageDialogViewButtonCallback) {
            buttonCallback = (MessageDialogViewButtonCallback) getParentFragment();
        } else if (context instanceof MessageDialogViewButtonCallback) {
            buttonCallback = (MessageDialogViewButtonCallback) context;
        }
    }

    @Override public void onDetach() {
        super.onDetach();
        callback = null;
    }

    @OnClick({R.id.cancel, R.id.ok, R.id.neutral}) public void onClick(@NonNull View view) {
        if (callback != null) {
            isAlreadyHidden = true;
            callback.onMessageDialogActionClicked(view.getId() == R.id.ok, getArguments().getBundle("bundle"));
        }
        if (buttonCallback != null) {
            int type = view.getId() == R.id.ok ? 0 : (view.getId() == R.id.negative ? 1 : 2);
            buttonCallback.onMessageDialogButtonClicked(type, getArguments().getBundle("bundle"));
        }
        dismiss();
    }

    @Override protected int layoutRes() {
        return R.layout.message_dialog;
    }

    @SuppressWarnings("ConstantConditions") @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        title.setText(bundle.getString("bundleTitle"));
        String msg = bundle.getString("bundleMsg");
        if (bundle.getBoolean("isMarkDown")) {
            if (msg != null) {
                message.setVisibility(View.GONE);
                prettifyWebView.setVisibility(View.VISIBLE);
                prettifyWebView.setGithubContent(msg, null, false, false);
                prettifyWebView.setNestedScrollingEnabled(false);
            }
        } else {
            message.setText(msg);
        }
        if (bundle != null) {
            boolean hideCancel = bundle.getBoolean("hideCancel");
            boolean showNeutral = bundle.getBoolean("showNeutral");
            if (hideCancel) cancel.setVisibility(View.GONE);
            if (!showNeutral) neutral.setVisibility(View.GONE);
            initButton(bundle);
        }
    }

    private void initButton(@NonNull Bundle bundle) {
        Bundle extra = bundle.getBundle("bundle");
        if (extra != null) {
            boolean yesNo = extra.getBoolean(BundleConstant.YES_NO_EXTRA);
            if (yesNo) {
                ok.setText(R.string.yes);
                cancel.setText(R.string.no);
            } else {
                boolean hideButtons = extra.getBoolean("hide_buttons");
                String primaryExtra = extra.getString("primary_extra");
                String secondaryExtra = extra.getString("secondary_extra");
                String neutralExtra = extra.getString("neutral_extra");
                if (hideButtons) {
                    ok.setVisibility(View.GONE);
                    cancel.setVisibility(View.GONE);
                    neutral.setVisibility(View.GONE);
                } else if (!InputHelper.isEmpty(neutralExtra)) {
                    if (!InputHelper.isEmpty(primaryExtra))
                        ok.setText(primaryExtra);
                    neutral.setText(neutralExtra);
                    neutral.setVisibility(View.VISIBLE);
                    ok.setVisibility(View.VISIBLE);
                } else if (!InputHelper.isEmpty(primaryExtra)) {
                    ok.setText(primaryExtra);
                    if (!InputHelper.isEmpty(secondaryExtra)) cancel.setText(secondaryExtra);
                    ok.setVisibility(View.VISIBLE);
                    cancel.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override protected void onDismissedByScrolling() {
        super.onDismissedByScrolling();
        if (callback != null) callback.onDialogDismissed();
    }

    @Override protected void onHidden() {
        if (callback != null) callback.onDialogDismissed();
        super.onHidden();
    }

    @NonNull public static MessageDialogView newInstance(@NonNull String bundleTitle, @NonNull String bundleMsg) {
        return newInstance(bundleTitle, bundleMsg, null);
    }

    @NonNull public static MessageDialogView newInstance(@NonNull String bundleTitle, @NonNull String bundleMsg, boolean isMarkDown) {
        return newInstance(bundleTitle, bundleMsg, isMarkDown, null);
    }

    @NonNull public static MessageDialogView newInstance(@NonNull String bundleTitle, @NonNull String bundleMsg,
                                                         boolean isMarkDown, boolean hideCancel) {
        return newInstance(bundleTitle, bundleMsg, isMarkDown, hideCancel, null);
    }

    @NonNull public static MessageDialogView newInstance(@NonNull String bundleTitle, @NonNull String bundleMsg, boolean isMarkDown,
                                                         @Nullable Bundle bundle) {
        MessageDialogView messageDialogView = new MessageDialogView();
        messageDialogView.setArguments(getBundle(bundleTitle, bundleMsg, isMarkDown, bundle, false, false));
        return messageDialogView;
    }

    @NonNull public static MessageDialogView newInstance(@NonNull String bundleTitle, @NonNull String bundleMsg, boolean isMarkDown,
                                                         boolean hideCancel, @Nullable Bundle bundle) {
        MessageDialogView messageDialogView = new MessageDialogView();
        messageDialogView.setArguments(getBundle(bundleTitle, bundleMsg, isMarkDown, bundle, hideCancel, false));
        return messageDialogView;
    }

    @NonNull public static MessageDialogView newInstance(@NonNull String bundleTitle, @NonNull String bundleMsg, boolean isMarkDown,
                                                         boolean hideCancel,boolean showNeutral, @Nullable Bundle bundle) {
        MessageDialogView messageDialogView = new MessageDialogView();
        messageDialogView.setArguments(getBundle(bundleTitle, bundleMsg, isMarkDown, bundle, hideCancel, showNeutral));
        return messageDialogView;
    }


    @NonNull public static MessageDialogView newInstance(@NonNull String bundleTitle, @NonNull String bundleMsg, @Nullable Bundle bundle) {
        return newInstance(bundleTitle, bundleMsg, false, bundle);
    }

    private static Bundle getBundle(String bundleTitle, String bundleMsg, boolean isMarkDown, Bundle bundle, boolean hideCancel, boolean showNeutral) {
        return Bundler.start()
                .put("bundleTitle", bundleTitle)
                .put("bundleMsg", bundleMsg)
                .put("bundle", bundle)
                .put("isMarkDown", isMarkDown)
                .put("hideCancel", hideCancel)
                .put("showNeutral", showNeutral)
                .end();
    }

    @NonNull public static Bundle getYesNoBundle(@NonNull Context context) {
        return Bundler.start()
                .put("primary_extra", context.getString(R.string.yes))
                .put("secondary_extra", context.getString(R.string.no))
                .end();
    }

    @NonNull public static Bundle getOkEditBundle(@NonNull Context context) {
        return Bundler.start()
                .put("primary_extra", context.getString(R.string.ok))
                .put("neutral_extra", context.getString(R.string.edit))
                .end();
    }
}
