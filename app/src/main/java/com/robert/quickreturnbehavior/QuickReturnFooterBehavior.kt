package com.robert.quickreturnbehavior

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Interpolator
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator

/**
 * CoordinatorLayout Behavior for a quick return footer
 *
 *
 * When a nested ScrollView is scrolled down, the quick return view will disappear.
 * When the ScrollView is scrolled back up, the quick return view will reappear.
 *
 * @author robert
 */
class QuickReturnFooterBehavior(context: Context?, attrs: AttributeSet?) : CoordinatorLayout.Behavior<View>(context, attrs) {
    private var animState = ANIM_STATE_NONE
    private var mDySinceDirectionChange = 0
    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: View, directTargetChild: View, target: View, axes: Int, type: Int): Boolean {
        return axes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
    }

    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: View, target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        if (dy > 0 && mDySinceDirectionChange < 0 || dy < 0 && mDySinceDirectionChange > 0) {
            // We detected a direction change -- reset our cumulative delta Y
            mDySinceDirectionChange = 0
        }
        mDySinceDirectionChange += dy
        if (mDySinceDirectionChange > child.height && !isOrWillBeHidden(child)) {
            hide(child)
        } else if (mDySinceDirectionChange < 0 && !isOrWillBeShown(child)) {
            show(child)
        }
    }

    private fun isOrWillBeHidden(view: View): Boolean {
        return if (view.visibility == View.VISIBLE) {
            animState == ANIM_STATE_HIDING
        } else {
            animState != ANIM_STATE_SHOWING
        }
    }

    private fun isOrWillBeShown(view: View): Boolean {
        return if (view.visibility != View.VISIBLE) {
            animState == ANIM_STATE_SHOWING
        } else {
            animState != ANIM_STATE_HIDING
        }
    }

    /**
     * Hide the quick return view.
     *
     *
     * Animates hiding the view, with the view sliding down and out of the screen.
     * After the view has disappeared, its visibility will change to GONE.
     *
     * @param view The quick return view
     */
    private fun hide(view: View) {
        view.animate().cancel()
        view.animate()
                .translationY(view.height.toFloat())
                .setInterpolator(INTERPOLATOR)
                .setDuration(200)
                .setListener(object : AnimatorListenerAdapter() {
                    private var isCanceled = false
                    override fun onAnimationStart(animation: Animator) {
                        animState = ANIM_STATE_HIDING
                        isCanceled = false
                        view.visibility = View.VISIBLE
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        isCanceled = true
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        animState = ANIM_STATE_NONE
                        if (!isCanceled) {
                            view.visibility = View.INVISIBLE
                        }
                    }
                })
    }

    /**
     * Show the quick return view.
     *
     *
     * Animates showing the view, with the view sliding up from the bottom of the screen.
     * After the view has reappeared, its visibility will change to VISIBLE.
     *
     * @param view The quick return view
     */
    private fun show(view: View) {
        view.animate().cancel()
        view.animate()
                .translationY(0f)
                .setInterpolator(INTERPOLATOR)
                .setDuration(200)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animator: Animator) {
                        animState = ANIM_STATE_SHOWING
                        view.visibility = View.VISIBLE
                    }

                    override fun onAnimationEnd(animator: Animator) {
                        animState = ANIM_STATE_NONE
                    }
                })
    }

    companion object {
        private val INTERPOLATOR: Interpolator = FastOutSlowInInterpolator()
        private const val ANIM_STATE_NONE = 0
        private const val ANIM_STATE_HIDING = 1
        private const val ANIM_STATE_SHOWING = 2
    }
}