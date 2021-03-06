package org.researchstack.backbone.ui.step.body;

import org.researchstack.backbone.R;
import org.researchstack.backbone.answerformat.SliderAnswerFormat;
import org.researchstack.backbone.result.StepResult;
import org.researchstack.backbone.step.QuestionStep;
import org.researchstack.backbone.step.Step;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by Anita on 6/15/2016.
 */
public class SliderStepBody implements StepBody {

    //TODO: Decide which step this should be compatible with
    private QuestionStep step;
    private StepResult<Integer> result;
    private SliderAnswerFormat format;

    private int viewType;
    private TextView barValue;
    private SeekBar seekBar;
    private TextView minText;
    private TextView maxText;
    private ImageView minImage;
    private ImageView maxImage;

    public SliderStepBody(Step step, StepResult result) {
        this.step = (QuestionStep) step;
        this.result = result == null ? new StepResult<>(step) : result;
        this.format = (SliderAnswerFormat) this.step.getAnswerFormat();
    }

    // TODO: Implement default and compact modes
    @Override
    public View getBodyView(int viewType, LayoutInflater inflater, ViewGroup parent) {
        this.viewType = viewType;

        View view = inflater.inflate(R.layout.rsb_slider_body, parent, false);

        seekBar = (SeekBar) view.findViewById(R.id.seekbar);
        barValue = (TextView) view.findViewById(R.id.bar_text);

        seekBar.setMax(format.getMaxVal());
        seekBar.setBackgroundColor(Color.WHITE);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i("SliderStepBody", "onProgressChanged progress: " + progress);
                barValue.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        setMinMaxLayout(view);

        if (result.getResult() != null) {
            seekBar.setProgress(result.getResult());
        }

        return view;
    }

    private void setMinMaxLayout(View view) {
        minText = (TextView) view.findViewById(R.id.min_text);
        maxText = (TextView) view.findViewById(R.id.max_text);

        if (format.getMinText() != null && !format.getMinText().isEmpty()) {
            minText.setText(format.getMinText());
        }
        if (format.getMaxText() != null && !format.getMaxText().isEmpty()) {
            maxText.setText(format.getMaxText());
        }

//        minImage = (ImageView) view.findViewById(R.id.min_image);
//        maxImage = (ImageView) view.findViewById(R.id.max_image);
//
//        if (format.getMinImage() != null){
//            minImage.setImageDrawable(format.getMinImage());
//        }
//        if (format.getMaxImage() != null){
//            maxImage.setImageDrawable(format.getMaxImage());
//        }
    }

    private View getViewForType(int viewType, LayoutInflater inflater, ViewGroup parent) {
        if (viewType == VIEW_TYPE_DEFAULT) {
            return initViewDefault(inflater, parent);
        } else if (viewType == VIEW_TYPE_COMPACT) {
            return initViewCompact(inflater, parent);
        } else {
            throw new IllegalArgumentException("Invalid View Type");
        }
    }

    private View initViewDefault(LayoutInflater inflater, ViewGroup parent) {
        return null;
    }

    private View initViewCompact(LayoutInflater inflater, ViewGroup parent) {
        return null;
    }

    @Override
    public StepResult getStepResult(boolean skipped) {
        if (skipped) {
            result.setResult(null);
        } else {
            Integer sliderVal = seekBar.getProgress();
            if (sliderVal != null) {
                result.setResult(sliderVal);
            }
        }

        return result;
    }

    @Override
    public BodyAnswer getBodyAnswerState() {
        if (seekBar == null) {
            return BodyAnswer.INVALID;
        }
        return format.validateAnswer(seekBar.getProgress());
    }
}
