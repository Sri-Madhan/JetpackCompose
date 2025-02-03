package com.example.compose.common

import android.app.Activity
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import com.example.compose.MainActivity

fun checkViewProperties(
    viewId: Int
): MutableMap<String, Any> {
    val activityScenario = ActivityScenario.launch(MainActivity::class.java)
    val view = getView(activityScenario, viewId)
    val allProperties = mutableMapOf<String, Any>()

    when {
        view?.let { isButton(it) } == true -> {
            allProperties.putAll(checkButtonProperties(view))
            allProperties.putAll(checkGeneralProperties(view))
        }
        view?.let { isEditText(it) } == true -> {
            allProperties.putAll(checkEditTextProperties(view))
            allProperties.putAll(checkGeneralProperties(view))
        }
        view?.let { isScrollable(it) } == true -> {
            allProperties.putAll(checkScrollableProperties(view))
            allProperties.putAll(checkGeneralProperties(view))
        }
        view?.let { isDialog(it) } == true -> {
            allProperties.putAll(checkDialogProperties(view))
            allProperties.putAll(checkGeneralProperties(view))
        }
        else -> view?.let { allProperties.putAll(checkGeneralProperties(it)) }
    }

    return allProperties
}

fun getView(activityScenario: ActivityScenario<out Activity>, viewId: Int): View? {
    var view: View? = null
    activityScenario.onActivity { activity ->
        view = activity.findViewById(viewId)
    }
    return view
}

private fun isButton(view: View): Boolean {
    return view is android.widget.Button
}

private fun isEditText(view: View): Boolean {
    return view is android.widget.EditText
}

private fun isScrollable(view: View): Boolean {
    return view is android.widget.ScrollView || view is android.widget.ListView || view is RecyclerView
}

private fun isDialog(view: View): Boolean {
    return view is android.app.Dialog
}

private fun checkGeneralProperties(view: View): Map<String, Boolean> {
    return mapOf(
        "isEnabled" to view.isEnabled,
        "isFocusable" to view.isFocusable,
        "isVisible" to (view.visibility == View.VISIBLE)
    )
}

private fun checkButtonProperties(view: View): Map<String, Boolean> {
    return mapOf(
        "isClickable" to view.isClickable,
        "isEnabled" to view.isEnabled
    )
}

private fun checkEditTextProperties(view: View): Map<String, Boolean> {
    return mapOf(
        "isInputField" to true,
        "isEnabled" to view.isEnabled,
        "isFocusable" to view.isFocusable
    )
}

private fun checkScrollableProperties(view: View): Map<String, Boolean> {
    return mapOf(
        "isScrollable" to true,
        "isVerticallyScrollable" to (view.canScrollVertically(1) || view.canScrollVertically(-1)),
        "isHorizontallyScrollable" to (view.canScrollHorizontally(1) || view.canScrollHorizontally(-1))
    )
}

private fun checkDialogProperties(view: View): Map<String, Boolean> {
    return mapOf(
        "isDialog" to (view is android.app.Dialog)
    )
}

