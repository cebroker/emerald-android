package co.condorlabs.customcomponents.imagecropview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import co.condorlabs.customcomponents.R;

public class AppCompatCropImageView extends AppCompatImageView {

    private Paint paint;
    private Point[] points;
    private Point start;
    private Point offset;
    private boolean isCenterFrame = false;
    private int minimumSideXLength;
    private int minimumSideYLength;
    private int strokeWidth;
    private int sideX;
    private int sideY;
    private int halfCorner;
    private int edgeColor;
    private int outsideColor;
    private int corner = 5;
    private boolean initialized = false;
    private int rectangleViewReferenceId = -1;
    public boolean parentDimens = true;
    private Drawable resizeDrawable0, resizeDrawable1, resizeDrawable2, resizeDrawable3;
    Context context;
    private boolean cropActivated = false;

    public AppCompatCropImageView(Context context) {
        super(context);
        this.context = context;
        init(null);
    }

    public AppCompatCropImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    public AppCompatCropImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    public void setCropActivated(Boolean cropActivated) {
        this.cropActivated = cropActivated;
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        cropActivated = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (parentDimens) {
            if (rectangleViewReferenceId != 0) {
                View view = ((View) getParent()).findViewById(rectangleViewReferenceId);

                if (view != null) {
                    points[0].x = (int) view.getX() - halfCorner;
                    points[0].y = (int) view.getY() - halfCorner;

                    points[1].x = view.getWidth() + (int) view.getX() - halfCorner;
                    points[1].y = (int) view.getY() - halfCorner;

                    points[2].x = (int) view.getX() - halfCorner;
                    points[2].y = view.getHeight() + (int) view.getY() - halfCorner;

                    points[3].x = view.getWidth() + (int) view.getX() - halfCorner;
                    points[3].y = view.getHeight() + (int) view.getY() - halfCorner;

                    sideX = points[1].x - points[0].x;
                    sideY = points[2].y - points[0].y;

                    parentDimens = false;
                }
            }
        }

        //set paint to draw edge, stroke
        if (initialized) {
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setColor(edgeColor);
            paint.setStrokeWidth(strokeWidth);
            //crop rectangle
            canvas.drawRect(
                    points[0].x + halfCorner,
                    points[0].y + halfCorner,
                    points[3].x + halfCorner,
                    points[3].y + halfCorner,
                    paint
            );
            //set paint to draw outside color, fill
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(outsideColor);
            int strokeHalfWidth = strokeWidth / 2;

            if (cropActivated) {
                //top rectangle
                canvas.drawRect(0, 0, getWidth(), points[0].y + halfCorner - strokeHalfWidth, paint);
                //left rectangle
                canvas.drawRect(0, points[0].y + halfCorner - strokeHalfWidth, points[0].x + halfCorner - strokeHalfWidth, getHeight(), paint);
                //right rectangle
                canvas.drawRect(points[1].x + halfCorner + strokeHalfWidth, points[1].y + halfCorner - strokeHalfWidth, getWidth(), points[3].y + halfCorner + strokeHalfWidth, paint);
                //bottom rectangle
                canvas.drawRect(points[0].x + halfCorner - strokeHalfWidth, points[2].y + halfCorner + strokeHalfWidth, getWidth(), getHeight(), paint);
            }
            //set bounds of drawables
            resizeDrawable0.setBounds(points[0].x, points[0].y, points[0].x + halfCorner * 2, points[0].y + halfCorner * 2);
            resizeDrawable1.setBounds(points[1].x, points[1].y, points[1].x + halfCorner * 2, points[1].y + halfCorner * 2);
            resizeDrawable2.setBounds(points[2].x, points[2].y, points[2].x + halfCorner * 2, points[2].y + halfCorner * 2);
            resizeDrawable3.setBounds(points[3].x, points[3].y, points[3].x + halfCorner * 2, points[3].y + halfCorner * 2);
            //place corner drawables
            if (cropActivated) {
                resizeDrawable0.draw(canvas);
                resizeDrawable1.draw(canvas);
                resizeDrawable2.draw(canvas);
                resizeDrawable3.draw(canvas);
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                start.x = (int) event.getX();
                start.y = (int) event.getY();
                corner = getCorner(start.x, start.y);
                offset = getOffset(start.x, start.y, corner);
                isCenterFrame = start.x >= points[0].x && start.x <= points[1].x &&
                        start.y >= points[0].y && start.y <= points[2].y;
                start.x = start.x - offset.x;
                start.y = start.y - offset.y;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (cropActivated) {
                    if (corner == 0) {
                        movePointOneOnY(event);
                        points[0].y = points[1].y;
                        movePointTwoOnX(event);
                        points[0].x = points[2].x;
                        invalidate();
                    } else if (corner == 1) {
                        movePointOneOnX(event);
                        points[3].x = points[1].x;
                        movePointOneOnY(event);
                        points[0].y = points[1].y;
                        invalidate();
                    } else if (corner == 2) {
                        movePointTwoOnY(event);
                        points[3].y = points[2].y;
                        movePointTwoOnX(event);
                        points[0].x = points[2].x;
                        invalidate();
                    } else if (corner == 3) {
                        movePointOneOnX(event);
                        points[3].x = points[1].x;
                        movePointTwoOnY(event);
                        points[3].y = points[2].y;
                        invalidate();
                    } else if (isCenterFrame) {
                        moveAllPoints(event);
                        invalidate();
                    }
                    break;
                }
            }
        }

        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void init(@Nullable AttributeSet attrs) {
        paint = new Paint();
        start = new Point();
        offset = new Point();
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.IconCropView, 0, 0);
        //initial dimensions
        //with default values the aspectRatio is 1.586 for standard cards' dimensions (85.60 × 53.98)mm
        minimumSideXLength = ta.getDimensionPixelSize(R.styleable.IconCropView_minimumSideX, 380);
        minimumSideYLength = ta.getDimensionPixelSize(R.styleable.IconCropView_minimumSideY, 240);
        strokeWidth = ta.getInteger(R.styleable.IconCropView_lineStrokeWidth, 4);
        sideX = minimumSideXLength;
        sideY = minimumSideYLength;
        halfCorner = (ta.getDimensionPixelSize(R.styleable.IconCropView_cornerSize, 20)) / 2;
        //colors
        int cornerColor = ta.getColor(R.styleable.IconCropView_cornerColor, Color.BLACK);
        edgeColor = ta.getColor(R.styleable.IconCropView_edgeColor, Color.WHITE);
        outsideColor = ta.getColor(R.styleable.IconCropView_outsideCropColor, Color.parseColor("#00000088"));
        //initialize corners;
        points = new Point[4];
        points[0] = new Point();
        points[1] = new Point();
        points[2] = new Point();
        points[3] = new Point();
        //init corner locations;
        //top left
        points[0].x = 0;
        points[0].y = 0;
        //top right
        points[1].x = minimumSideXLength;
        points[1].y = 0;
        //bottom left
        points[2].x = 0;
        points[2].y = minimumSideYLength;
        //bottom right
        points[3].x = minimumSideXLength;
        points[3].y = minimumSideYLength;
        rectangleViewReferenceId = ta.getResourceId(R.styleable.IconCropView_linkedView, 0);
        //init drawables
        resizeDrawable0 = ta.getDrawable(R.styleable.IconCropView_resizeCornerDrawable);
        resizeDrawable1 = ta.getDrawable(R.styleable.IconCropView_resizeCornerDrawable);
        resizeDrawable2 = ta.getDrawable(R.styleable.IconCropView_resizeCornerDrawable);
        resizeDrawable3 = ta.getDrawable(R.styleable.IconCropView_resizeCornerDrawable);
        //set drawable colors
        resizeDrawable0.setTint(cornerColor);
        resizeDrawable1.setTint(cornerColor);
        resizeDrawable2.setTint(cornerColor);
        resizeDrawable3.setTint(cornerColor);
        //recycle attributes
        ta.recycle();
        //set initialized to true
        initialized = true;
    }

    private void moveAllPoints(MotionEvent event) {
        points[0].x = Math.max(points[0].x + (int) Math.min(Math.floor((event.getX() - start.x - offset.x)), Math.floor(getWidth() - points[0].x - 2 * halfCorner - sideX)), 0);
        points[1].x = Math.max(points[1].x + (int) Math.min(Math.floor((event.getX() - start.x - offset.x)), Math.floor(getWidth() - points[1].x - 2 * halfCorner)), sideX);
        points[2].x = Math.max(points[2].x + (int) Math.min(Math.floor((event.getX() - start.x - offset.x)), Math.floor(getWidth() - points[2].x - 2 * halfCorner - sideX)), 0);
        points[3].x = Math.max(points[3].x + (int) Math.min(Math.floor((event.getX() - start.x - offset.x)), Math.floor(getWidth() - points[3].x - 2 * halfCorner)), sideX);
        points[0].y = Math.max(points[0].y + (int) Math.min(Math.floor((event.getY() - start.y - offset.y)), Math.floor(getHeight() - points[0].y - 2 * halfCorner - sideY)), 0);
        points[1].y = Math.max(points[1].y + (int) Math.min(Math.floor((event.getY() - start.y - offset.y)), Math.floor(getHeight() - points[1].y - 2 * halfCorner - sideY)), 0);
        points[2].y = Math.max(points[2].y + (int) Math.min(Math.floor((event.getY() - start.y - offset.y)), Math.floor(getHeight() - points[2].y - 2 * halfCorner)), sideY);
        points[3].y = Math.max(points[3].y + (int) Math.min(Math.floor((event.getY() - start.y - offset.y)), Math.floor(getHeight() - points[3].y - 2 * halfCorner)), sideY);
        start.x = points[0].x;
        start.y = points[0].y;
    }

    private void movePointOneOnX(MotionEvent event) {
        sideX = Math.min(Math.max(minimumSideXLength, (int) (sideX + Math.floor(event.getX()) - start.x - offset.x)), sideX + (getWidth() - points[1].x - 2 * halfCorner));
        start.x = points[1].x = points[0].x + sideX;
    }

    private void movePointOneOnY(MotionEvent event) {
        sideY = points[3].y - Math.max(Math.min((int) (Math.floor(event.getY()) - offset.y), points[3].y - minimumSideYLength), 0);
        start.y = points[1].y = points[3].y - sideY;
    }

    private void movePointTwoOnY(MotionEvent event) {
        sideY = Math.min(Math.max(minimumSideYLength, (int) (sideY + Math.floor(event.getY()) - start.y - offset.y)), sideY + (getHeight() - points[2].y - 2 * halfCorner));
        start.y = points[2].y = points[0].y + sideY;
    }

    private void movePointTwoOnX(MotionEvent event) {
        sideX = points[3].x - Math.max(Math.min((int) (Math.floor(event.getX()) - offset.x), points[3].x - minimumSideXLength), 0);
        start.x = points[2].x = points[3].x - sideX;
    }

    private int getCorner(float x, float y) {
        int corner = 5;
        for (int i = 0; i < points.length; i++) {
            float dx = x - points[i].x;
            float dy = y - points[i].y;
            int max = halfCorner * 2;
            if (dx <= max && dx >= 0 && dy <= max && dy >= 0) {
                return i;
            }
        }
        return corner;
    }

    private Point getOffset(int left, int top, int corner) {
        Point offset = new Point();
        if (corner == 5) {
            offset.x = left - points[0].x;
            offset.y = top - points[0].y;
        } else {
            offset.x = left - points[corner].x;
            offset.y = top - points[corner].y;
        }
        return offset;
    }

    public Bitmap cropImage() {
        Bitmap source = getBitmap();
        if (source != null) {
            int diffX = source.getWidth() - getWidth();
            int diffY = source.getHeight() - getHeight();
            if (diffX <= 0) {
                diffX = 0;
            } else {
                diffX = diffX / 2;
            }
            if (diffY <= 0) {
                diffY = 0;
            } else {
                diffY = diffY / 2;
            }

            return Bitmap.createBitmap(
                    Bitmap.createBitmap(source, diffX, diffY, Math.min(source.getWidth(), getWidth()), Math.min(source.getHeight(), getHeight())),
                    points[0].x + halfCorner,
                    points[0].y + halfCorner,
                    sideX,
                    sideY
            );
        } else {
            return null;
        }
    }

    private Bitmap getBitmap() {
        return ((BitmapDrawable) getDrawable()).getBitmap();
    }
}