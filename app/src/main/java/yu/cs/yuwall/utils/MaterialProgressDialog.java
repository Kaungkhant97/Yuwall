package yu.cs.yuwall.utils;

import android.app.Dialog;
import android.content.Context;

import yu.cs.yuwall.R;


public class MaterialProgressDialog extends Dialog {
    MaterialProgressBar progress1;

    Context mContext;
    MaterialProgressDialog dialog;
	public MaterialProgressDialog(Context context) {
        super(context);
        this.mContext=context;
	}
	public MaterialProgressDialog(Context context, int theme) {
		super(context, theme);
	}

	public MaterialProgressDialog show(CharSequence message) {

	    dialog = new MaterialProgressDialog(mContext, R.style.ProgressDialog);
		dialog.setContentView(R.layout.view_material_progress);

        progress1 = (MaterialProgressBar) dialog.findViewById (R.id.progress1);


        progress1.setColorSchemeResources(R.color.red,R.color.green,R.color.blue,R.color.orange,R.color.purple,R.color.yellow);
		dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        if(dialog!= null) {
             dialog.show ();
        }
		return dialog;
	}
    public MaterialProgressDialog dismiss(CharSequence message) {
        if(dialog!=null) {
            dialog.dismiss();
        }

            return dialog;

    }


    @Override
    public void dismiss() {
        super.dismiss();
    }
}
