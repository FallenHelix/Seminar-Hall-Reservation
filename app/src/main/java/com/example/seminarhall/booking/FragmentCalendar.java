package com.example.seminarhall.booking;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.seminarhall.Hall;
import com.example.seminarhall.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.timessquare.CalendarPickerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FragmentCalendar extends Fragment implements CalendarPickerView.OnDateSelectedListener,
        CalendarPickerView.DateSelectableFilter {

    private static final String TAG = "FragmentCalendar";
    public CalendarPickerView calendarPickerView;
    BookingHelper helper = new BookingHelper();
    private OnFragmentInteractionListener mListener;
    Hall hall;
    Date today = Calendar.getInstance().getTime();


    private List<Date> BookedDates;//dates that are needed to be highlighted, indicating a booked event;
    private static List<String> singleDates;//dates that are needed to be highlighted, indicating a singled booked event;
    private static List<Map<String, String>> array = new ArrayList<>();

    private List<String> BookedDatesText;
    private static Map<String, String> id = new HashMap<>();
    private static Map<String, String> id2 = new HashMap<>();


    public static String getSingleId(String key) {
        return id.get(key);
    }

    public static String stat(String key) {
        return id2.get(key);
    }

    public void setHall(Hall h) {
        this.hall = h;
    }

    private void getBookedDates() {
        Log.d(TAG, "Today: " + today);
        Log.d(TAG, "getBookedDates: ");
        CollectionReference db = FirebaseFirestore.getInstance().collection("Main/Reservation/Active");
        CollectionReference db2 = FirebaseFirestore.getInstance().collection("Main/Reservation/Closed");
//        db.whereEqualTo("hallId", hall.getKey())
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    private Map<String, Object> Temp;
//                    int days;
//                    String k;
//
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        for (QueryDocumentSnapshot x : queryDocumentSnapshots) {
//                            Temp = x.getData();
////                            x.get
//                            days = (int) ((long) x.get("noOfDays"));
//                            Log.d(TAG, "Days: " + days);
//                            if (days == 1) {
//                                String d=((List<String>)Temp.get("days")).get(0);
//
//                                singleDates.add(d);
//                                k=(String) Temp.get("key");
//                                id.put(d,k);
//                                id2.put(d, "Active");
//                            }
//                            else
//                            BookedDatesText.addAll((List<String>)Temp.get("days"));
//                        }
//                        setUpCalendar();
//                        toDatesArray(BookedDatesText);
//                        toDatesArray(singleDates);
//                    }
//                });
//
//         db = FirebaseFirestore.getInstance().collection("Main/Reservation/Closed");
//        db.whereEqualTo("hallId", hall.getKey())
//                .whereEqualTo("Status",true)
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    private Map<String, Object> Temp;
//                    int days;
//                    String k;
//
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        for (QueryDocumentSnapshot x : queryDocumentSnapshots) {
//                            Temp = x.getData();
////                            x.get
//                            days = (int) ((long) x.get("noOfDays"));
//                            Log.d(TAG, "Days: " + days);
//                            if (days == 1) {
//                                String d=((List<String>)Temp.get("days")).get(0);
//
//                                singleDates.add(d);
//                                k=(String) Temp.get("key");
//                                id.put(d,k);
//                                id2.put(d, "Closed");
//                            }
//                            else
//                                BookedDatesText.addAll((List<String>) Temp.get("days"));
//                        }
//                        setUpCalendar();
//                        toDatesArray(BookedDatesText);
//                        toDatesArray(singleDates);
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d(TAG, "onFailure: "+e);
//            }
//        });
        Task t1 = db.get();
        Task t2 = db2.whereEqualTo("hallId", hall.getKey())
                .whereEqualTo("Status", true)
                .get();
        Task<List<QuerySnapshot>> allTask = Tasks.whenAllSuccess(t1, t2);
        allTask.addOnSuccessListener(new OnSuccessListener<List<QuerySnapshot>>() {
            @Override
            public void onSuccess(List<QuerySnapshot> querySnapshots) {
                int days;
                String k;
                Map<String, Object> Temp = new HashMap<>();
                for (QuerySnapshot queryDocumentSnapshots : querySnapshots) {
                    for (QueryDocumentSnapshot x : queryDocumentSnapshots) {
                        Temp = x.getData();

                        days = (int) ((long) Temp.get("noOfDays"));
                        Log.d(TAG, "Days: " + days);
                        if (days == 1) {
                            String d = ((List<String>) Temp.get("days")).get(0);
                            singleDates.add(d);
                            k = (String) Temp.get("key");
                            id.put(d, k);
                            id2.put(d, "Active");
                        } else
                            BookedDatesText.addAll((List<String>) Temp.get("days"));
                    }
                    setUpCalendar();
                    toDatesArray(BookedDatesText);
                    toDatesArray(singleDates);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e);
            }
        });
    }

    public static List<String> sD() {
        return singleDates;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        setViews(view);
        setUpCalendar();
        return view;
    }

    private void setViews(View view) {
        Log.d(TAG, "setViews: ");

        calendarPickerView = view.findViewById(R.id.calendar);
        calendarPickerView.setOnDateSelectedListener(this);
        BookedDates = new ArrayList<>();
        BookedDatesText = new ArrayList<>();
        calendarPickerView.setDateSelectableFilter(this);
        getBookedDates();
        //setting up lists
        singleDates = new ArrayList<>();
        BookedDatesText = new ArrayList<>();
    }

    private void setUpCalendar() {
        Log.d(TAG, "setUpCalendar: ");
        Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.rc_medium);
        calendarPickerView.setTitleTypeface(typeface);
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 5);
        calendarPickerView.init(today, c.getTime()).inMode(CalendarPickerView.SelectionMode.RANGE);
        typeface = ResourcesCompat.getFont(getActivity(), R.font.sf_display_medium);
        calendarPickerView.setDateTypeface(typeface);
        calendarPickerView.setHapticFeedbackEnabled(true);
        calendarPickerView.highlightDates(BookedDates);
    }


    private void getDates() {
        Log.d(TAG, "getDates: ");
        List<Date> dates = calendarPickerView.getSelectedDates();
        if (dates == null || dates.size() > 10) {
            Toast.makeText(getContext(), "Max Number of days: 10", Toast.LENGTH_SHORT).show();
            mListener.onFragmentInteraction(null);
            mListener.clash(false);
            setUpCalendar();
            return;
        }
        List<String> selectedDates = new ArrayList<>();
        for (Date x : dates) {
            String temp = DateFormat.getDateInstance(DateFormat.DATE_FIELD, Locale.ITALY).format(x);
            if (BookedDatesText.contains(temp)) {
                setUpCalendar();
                Log.d(TAG, "getDates: clash");
                Toast.makeText(getContext(), "Sorry Date has been Occupied", Toast.LENGTH_SHORT).show();
                return;
            } else if (this.singleDates.contains(temp)) {
                if (dates.size() > 1) {
                    setUpCalendar();
                    return;
                } else {
                    selectedDates.add(temp);
                    mListener.onFragmentInteraction(selectedDates);
                    mListener.clash(true);
                    return;
                }
//                               Toast.makeText(getContext(), "Single Reservation day", Toast.LENGTH_SHORT).show();
            }
            selectedDates.add(temp);
            Log.d(TAG, "getDates: " + dates.size());
        }
        mListener.onFragmentInteraction(selectedDates);
        mListener.clash(false);
    }


    @Override
    public void onDateSelected(Date date) {
        getDates();
    }

    private void toDatesArray(List<String> dates) {
        Log.d(TAG, "toDatesArray: ");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        for (int i = 0; i < dates.size(); i++) {
            Date t = null;
            try {
                t = sdf.parse(dates.get(i));
//                                    BookedDates.add(t);

                if (t.after(today)) {
                    BookedDates.add(t);
                }
                else
                {
                    Log.d(TAG, "Before Date: "+t);
                }
            } catch (ParseException e) {
                Log.d(TAG, "Exception In Dates");
                return;
            }
        }
        for (Date x : BookedDates) {
            Log.d(TAG, x.toString());
        }
        calendarPickerView.highlightDates(BookedDates);
    }

    @Override
    public void onDateUnselected(Date date) {

    }

    public void sendBack(List<String> sendBackText) {
        if (mListener != null) {
            mListener.onFragmentInteraction(sendBackText);
        }
    }

    public void sendClash(boolean t) {
        if (mListener != null) {
            mListener.clash(t);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean isDateSelectable(Date date) {
        return true;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(List<String> sendBackText);

        void clash(boolean stat);
    }

}
