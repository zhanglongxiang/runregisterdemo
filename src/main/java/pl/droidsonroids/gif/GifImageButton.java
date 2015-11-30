package pl.droidsonroids.gif;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.IOException;

/**
 * An {@link android.widget.ImageButton} which tries treating background and src as {@link pl.droidsonroids.gif.GifDrawable}
 *
 * @author koral--
 */
public class GifImageButton extends ImageButton {
    /**
     * A corresponding superclass constructor wrapper.
     *
     * @param context
     * @see android.widget.ImageView#ImageView(android.content.Context)
     */
    public GifImageButton(Context context) {
        super(context);
    }

    /**
     * Like equivalent from superclass but also try to interpret src and background
     * attributes as {@link pl.droidsonroids.gif.GifDrawable}.
     *
     * @param context
     * @param attrs
     * @see android.widget.ImageView#ImageView(android.content.Context, android.util.AttributeSet)
     */
    public GifImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        trySetGifDrawable(attrs, getResources());
    }

    /**
     * Like equivalent from superclass but also try to interpret src and background
     * attributes as GIFs.
     *
     * @param context
     * @param attrs
     * @param defStyle
     * @see android.widget.ImageView#ImageView(android.content.Context, android.util.AttributeSet, int)
     */
    public GifImageButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        trySetGifDrawable(attrs, getResources());
    }

    @Override
    public void setImageResource(int resId) {
        setResource(true, resId, getResources());
    }

    @Override
    public void setBackgroundResource(int resId) {
        setResource(false, resId, getResources());
    }

    void trySetGifDrawable(AttributeSet attrs, Resources res) {
        if (attrs != null && res != null && !isInEditMode()) {
            int resId = attrs.getAttributeResourceValue(GifImageView.ANDROID_NS, "src", -1);
            if (resId > 0 && "drawable".equals(res.getResourceTypeName(resId)))
                setResource(true, resId, res);

            resId = attrs.getAttributeResourceValue(GifImageView.ANDROID_NS, "background", -1);
            if (resId > 0 && "drawable".equals(res.getResourceTypeName(resId)))
                setResource(false, resId, res);
        }
    }

    @SuppressWarnings("deprecation")
        //new method not available on older API levels
    void setResource(boolean isSrc, int resId, Resources res) {
        try {
            GifDrawable d = new GifDrawable(res, resId);
            if (isSrc)
                setImageDrawable(d);
            else
                setBackgroundDrawable(d);
            return;
        } catch (Exception ignored) {
            //ignored
        }
        if (isSrc)
            super.setImageResource(resId);
        else
            super.setBackgroundResource(resId);
    }

    /**
     * Sets the content of this GifImageView to the specified Uri.
     * If uri destination is not a GIF then {@link android.widget.ImageButton#setImageURI(android.net.Uri)}
     * is called as fallback.
     * For supported URI schemes see: {@link android.content.ContentResolver#openAssetFileDescriptor(android.net.Uri, String)}.
     *
     * @param uri The Uri of an image
     */
    @Override
    public void setImageURI(Uri uri) {
        if (uri != null)
            try {
                setImageDrawable(new GifDrawable(getContext().getContentResolver(), uri));
                return;
            } catch (IOException ignored) {
                //ignored
            }
        super.setImageURI(uri);
    }
}
