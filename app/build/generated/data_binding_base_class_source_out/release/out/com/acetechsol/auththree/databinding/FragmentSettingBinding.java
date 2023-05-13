// Generated by view binder compiler. Do not edit!
package com.acetechsol.auththree.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.acetechsol.auththree.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentSettingBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final Button backupBtn;

  @NonNull
  public final Button restoreBtn;

  private FragmentSettingBinding(@NonNull LinearLayout rootView, @NonNull Button backupBtn,
      @NonNull Button restoreBtn) {
    this.rootView = rootView;
    this.backupBtn = backupBtn;
    this.restoreBtn = restoreBtn;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentSettingBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentSettingBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_setting, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentSettingBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.backup_Btn;
      Button backupBtn = ViewBindings.findChildViewById(rootView, id);
      if (backupBtn == null) {
        break missingId;
      }

      id = R.id.restore_Btn;
      Button restoreBtn = ViewBindings.findChildViewById(rootView, id);
      if (restoreBtn == null) {
        break missingId;
      }

      return new FragmentSettingBinding((LinearLayout) rootView, backupBtn, restoreBtn);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
