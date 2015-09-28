package org.billthefarmer.location;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.util.AttributeSet;
import android.view.View;

public class StatusView extends View
{
    private GpsStatus status;
    private Iterable<GpsSatellite> satellites;

    private int width;
    private int height;

    private int maxSatellites;
    private float maxPrn = 32;

    private Paint paint;

    // StatusView

    public StatusView(Context context, AttributeSet attrs)
    {
	super(context, attrs);

	paint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
	super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	// Get offered dimension

	int w = MeasureSpec.getSize(widthMeasureSpec);

	// Set wanted dimensions

	setMeasuredDimension(w, w / 12);
    }

    // On size changed

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
	super.onSizeChanged(w, h, oldw, oldh);

	// Get dimensions

	width = w;
	height = h;
    }

    // On draw

    @Override
    protected void onDraw(Canvas canvas)
    {
	canvas.translate(0, height - 2);

	float rectWidth = width / (maxPrn + 1);

	paint.setColor(Color.WHITE);
	canvas.drawLine(0, 1, width, 1, paint);

	if (satellites != null)
	{
	    for (GpsSatellite satellite: satellites)
	    {
		float x = rectWidth * satellite.getPrn();
		float y = satellite.getSnr() / 36f * height;

		if (satellite.usedInFix())
		    paint.setColor(Color.GREEN);

		else
		    paint.setColor(Color.RED);

		canvas.drawRect(x + 1, -y, x + rectWidth - 1, 0, paint);
	    }
	}
    }

    // Update status

    public void updateStatus(GpsStatus s)
    {
	status = s;

	maxSatellites = status.getMaxSatellites();
	satellites = status.getSatellites();

	for (GpsSatellite satellite: satellites)
	    if (maxPrn < satellite.getPrn())
		maxPrn = satellite.getPrn();

	invalidate();
    }
}
