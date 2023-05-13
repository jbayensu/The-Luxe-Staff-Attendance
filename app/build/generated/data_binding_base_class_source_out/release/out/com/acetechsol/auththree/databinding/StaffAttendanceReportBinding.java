// Generated by view binder compiler. Do not edit!
package com.acetechsol.auththree.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.acetechsol.auththree.R;
import de.hdodenhof.circleimageview.CircleImageView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class StaffAttendanceReportBinding implements ViewBinding {
  @NonNull
  private final CardView rootView;

  @NonNull
  public final ImageButton moreActionIB;

  @NonNull
  public final TextView staffRecordDepartmentET;

  @NonNull
  public final TextView staffRecordFullNameET;

  @NonNull
  public final TextView staffRecordPhoneNumberET;

  @NonNull
  public final CircleImageView staffRecordProfilePictureCIV;

  private StaffAttendanceReportBinding(@NonNull CardView rootView,
      @NonNull ImageButton moreActionIB, @NonNull TextView staffRecordDepartmentET,
      @NonNull TextView staffRecordFullNameET, @NonNull TextView staffRecordPhoneNumberET,
      @NonNull CircleImageView staffRecordProfilePictureCIV) {
    this.rootView = rootView;
    this.moreActionIB = moreActionIB;
    this.staffRecordDepartmentET = staffRecordDepartmentET;
    this.staffRecordFullNameET = staffRecordFullNameET;
    this.staffRecordPhoneNumberET = staffRecordPhoneNumberET;
    this.staffRecordProfilePictureCIV = staffRecordProfilePictureCIV;
  }

  @Override
  @NonNull
  public CardView getRoot() {
    return rootView;
  }

  @NonNull
  public static StaffAttendanceReportBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static StaffAttendanceReportBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.staff_attendance_report, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static StaffAttendanceReportBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.more_action_IB;
      ImageButton moreActionIB = ViewBindings.findChildViewById(rootView, id);
      if (moreActionIB == null) {
        break missingId;
      }

      id = R.id.staff_record_department_ET;
      TextView staffRecordDepartmentET = ViewBindings.findChildViewById(rootView, id);
      if (staffRecordDepartmentET == null) {
        break missingId;
      }

      id = R.id.staff_record_full_name_ET;
      TextView staffRecordFullNameET = ViewBindings.findChildViewById(rootView, id);
      if (staffRecordFullNameET == null) {
        break missingId;
      }

      id = R.id.staff_record_phone_number_ET;
      TextView staffRecordPhoneNumberET = ViewBindings.findChildViewById(rootView, id);
      if (staffRecordPhoneNumberET == null) {
        break missingId;
      }

      id = R.id.staff_record_profile_picture_CIV;
      CircleImageView staffRecordProfilePictureCIV = ViewBindings.findChildViewById(rootView, id);
      if (staffRecordProfilePictureCIV == null) {
        break missingId;
      }

      return new StaffAttendanceReportBinding((CardView) rootView, moreActionIB,
          staffRecordDepartmentET, staffRecordFullNameET, staffRecordPhoneNumberET,
          staffRecordProfilePictureCIV);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
