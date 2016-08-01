package com.e16din.goeurotest.screens.main;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.e16din.goeurotest.R;
import com.e16din.goeurotest.TheApplication;
import com.e16din.goeurotest.Utils;
import com.e16din.goeurotest.model.City;
import com.e16din.lightutils.Get;
import com.e16din.lightutils.tools.SimpleTextWatcher;
import com.e16din.lightutils.utils.U;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final String TAG_DATE_PICKER_DIALOG = "DatePickerDialog";


    @BindView(R.id.vFromCityField) AutoCompleteTextView vFromCityField;
    @BindView(R.id.vToCityField) AutoCompleteTextView vToCityField;

    @BindView(R.id.vFromCityProgressBar) View vFromCityProgressBar;
    @BindView(R.id.vToCityProgressBar) View vToCityProgressBar;

    @BindView(R.id.vDateField) EditText vDateField;

    @BindView(R.id.vSearch) AppCompatButton vSearch;


    private final String mCurrentLocale = Locale.getDefault().getCountry().toLowerCase();

    private Call<ArrayList<City>> mCallGetCities;

    // mLastEntered != mLastSelected
    private String mLastSelected = "";
    private String mLastEntered = "";
    // example for RU locale
    // mLastEntered == Rostov
    // mLastSelected == Ростов-на-Дону

    private CitiesAdapter mCommonAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mCommonAdapter = new CitiesAdapter(this);

        vFromCityField.setAdapter(mCommonAdapter);
        vToCityField.setAdapter(mCommonAdapter);

        /// on text changed

        vFromCityField.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                MainActivity.this.afterTextChanged(s.toString(), vFromCityField, vFromCityProgressBar);
            }
        });
        vToCityField.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                MainActivity.this.afterTextChanged(s.toString(), vToCityField, vToCityProgressBar);
            }
        });
        vDateField.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                updateSearchButtonState();
            }
        });

        /// on item click

        vFromCityField.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onCitySelect(mCommonAdapter.getItem(i), vFromCityField);
            }
        });
        vToCityField.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onCitySelect(mCommonAdapter.getItem(i), vToCityField);
            }
        });

        //on focus change

        final View.OnFocusChangeListener onFocusChange = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                mLastEntered = "";
                mLastSelected = "";
                updateSearchButtonState();
            }
        };
        vFromCityField.setOnFocusChangeListener(onFocusChange);
        vToCityField.setOnFocusChangeListener(onFocusChange);
        vDateField.setOnFocusChangeListener(onFocusChange);
    }

    private void afterTextChanged(String newText, AutoCompleteTextView vCity, View vProgressBar) {
        if (!mLastSelected.equals(newText) && !mLastEntered.equals(newText)) {
            updateCities(vCity, vProgressBar);
            updateSearchButtonState();
        }

        mLastEntered = newText;
    }

    private void onCitySelect(String selectedCity, AutoCompleteTextView vCity) {
        mLastSelected = selectedCity;

        vCity.setText(selectedCity);
        vCity.setSelection(vCity.length());

        vCity.dismissDropDown();
        updateSearchButtonState();
    }

    @OnClick(R.id.vCalendarButton)
    public void selectDate(View v) {
        Calendar now = Calendar.getInstance();
        DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        final String date = getString(R.string.date,
                                Utils.addZero(dayOfMonth),
                                Utils.addZero(monthOfYear),
                                year);

                        vDateField.setText(date);
                        updateSearchButtonState();
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        ).show(getFragmentManager(), TAG_DATE_PICKER_DIALOG);
    }

    @OnClick(R.id.vSearch)
    public void search(View v) {//todo: add search logic
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.app_name)
                .setMessage(R.string.search_is_not_yet_implemented)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    private void updateSearchButtonState() {
        final boolean enabled = !U.areEmpty(vFromCityField.getText(), vToCityField.getText(), vDateField.getText());
        vSearch.setEnabled(enabled);
    }

    private void updateCities(final AutoCompleteTextView vCity, final View vProgressBar) {
        final String cityName = Get.string(vCity);

        if (mCallGetCities != null) {
            mCallGetCities.cancel();
        }

        U.showView(vProgressBar);

        mCallGetCities = TheApplication.getServices().getCities(mCurrentLocale, cityName);
        mCallGetCities.enqueue(new Callback<ArrayList<City>>() {
            @Override
            public void onResponse(Call<ArrayList<City>> call, Response<ArrayList<City>> response) {
                U.hideView(vProgressBar);
                CitiesAdapter adapter = (CitiesAdapter) vCity.getAdapter();
                adapter.clear();

                if (response == null || response.body() == null) {
                    //nothing to show
                    return;
                }// else

                adapter.addAll(response.body());

                vCity.showDropDown();

                updateSearchButtonState();
            }

            @Override
            public void onFailure(Call<ArrayList<City>> call, Throwable t) {
                if (!call.isCanceled()) {
                    U.hideView(vProgressBar);
                }

                updateSearchButtonState();
                t.printStackTrace();
            }
        });
    }
}
