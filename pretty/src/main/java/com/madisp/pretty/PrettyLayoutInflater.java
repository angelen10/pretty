package com.madisp.pretty;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import com.android.internal.policy.impl.PhoneLayoutInflater;

public class PrettyLayoutInflater extends PhoneLayoutInflater {

	private LayoutInflater.Factory2 wrappedFactory;
	private Pretty pretty;

	public PrettyLayoutInflater(Pretty pretty, final Activity activity) {
		super(activity);

		this.pretty = pretty;

		// if the activity is a FragmentActivity from the support lib then lets wrap it
		// so the <fragment> tags still work
		try {
			Class<?> fragAct = Class.forName("android.support.v4.app.FragmentActivity");
			if (fragAct != null && fragAct.isInstance(activity)) {
				// FragmentActivity is a Factory1, not Factory2
				wrappedFactory = new LayoutInflater.Factory2() {
					@Override
					public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
						return onCreateView(name, context, attrs);
					}

					@Override
					public View onCreateView(String name, Context context, AttributeSet attrs) {
						return activity.onCreateView(name, context, attrs);
					}
				};
			}
		} catch (Exception ignored) { /* ignored */ }
		super.setFactory2(new PrettyLayoutFactory(this, wrappedFactory, pretty));
	}

	protected PrettyLayoutInflater(LayoutInflater original, Context newContext, LayoutInflater.Factory2 wrappedFactory, Pretty pretty) {
		super(original, newContext);
		this.pretty = pretty;
		super.setFactory2(new PrettyLayoutFactory(this, wrappedFactory, pretty));
	}

	@Override
	public LayoutInflater cloneInContext(Context newContext) {
		return new PrettyLayoutInflater(this, newContext, wrappedFactory, pretty);
	}

	@Override
	public void setFactory(Factory factory) {
		// outright evil, warn here?
	}

	@Override
	public void setFactory2(Factory2 factory) {
		// outright evil, warn here?
	}
}
