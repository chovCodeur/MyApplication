package com.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

/**
 * Classe permettant de donner un aspect particulier au clic des ImageButton.
 */
public class TouchHighlightImageButton extends android.support.v7.widget.AppCompatImageButton {
    /**
     * The highlight drawable. This generally a {@link android.graphics.drawable.StateListDrawable}
     * that's transparent in the default state, and contains a semi-transparent overlay
     * for the focused and pressed states.
     */
    private Drawable mForegroundDrawable;

    /**
     * Création d'un rectangle entourant le bouton.
     */
    private Rect mCachedBounds = new Rect();

    /**
     * Constructeur de classe.
     * @param context Context d'appel dans l'application.
     */
    public TouchHighlightImageButton(Context context) {
        super(context);
        init();
    }

    /**
     * Constructeur surchargé de la classe.
     * @param context Context d'appel dans l'application.
     * @param attrs
     */
    public TouchHighlightImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Constructeur surchargé de la classe.
     * @param context Context d'appel dans l'application.
     * @param attrs
     * @param defStyle
     */
    public TouchHighlightImageButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * Méthode permettant d'initialiser la création du style du ImageButton.
     */
    private void init() {
        // Reset les paramètres par défaut de l'ImageButton en terme de background et de padding
        setBackgroundColor(0);
        setPadding(0, 0, 0, 0);

        // Récupératiion de la ressource drawable assignée à l'attribut android.R.attr.selectableItemBackground du thème courant
        TypedArray a = getContext()
                .obtainStyledAttributes(new int[]{android.R.attr.selectableItemBackground});
        mForegroundDrawable = a.getDrawable(0);
        mForegroundDrawable.setCallback(this);
        a.recycle();
    }

    /**
     * Méthode permettant de notifier un changement du style de l'ImageButton.
     */
    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();

        // Mise à jour de l'état du highlight drawable pour correspondre à l'état du bouton
        if (mForegroundDrawable.isStateful()) {
            mForegroundDrawable.setState(getDrawableState());
        }

        // On déclenche le refresh de l'affichage
        invalidate();
    }

    /**
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {

        // On dessine l'image
        super.onDraw(canvas);

        // On dessine le highlight par dessus. Si le bouton n'est ni cliqué ni focus, le highlight sera transparent. Seule l'image est affichée
        // will be drawn.
        mForegroundDrawable.setBounds(mCachedBounds);
        mForegroundDrawable.draw(canvas);
    }

    /**
     * Méthode permettant de changer la taille des limites lorsque la taille de l'ImageButton change.
     * @param w int contenant la nouvelle largeur de l'ImageButton.
     * @param h int contenant la nouvelle hauteur de l'ImageButton.
     * @param oldw int contenant l'ancienne largeur de l'ImageButton.
     * @param oldh int contenant l'ancienne hauteur de l'ImageButton.
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // On cache les limites de la vue
        mCachedBounds.set(0, 0, w, h);
    }
}
