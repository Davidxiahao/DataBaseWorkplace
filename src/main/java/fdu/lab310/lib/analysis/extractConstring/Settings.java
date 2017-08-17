package fdu.lab310.lib.analysis.extractConstring;

import soot.Scene;
import soot.G;
import soot.options.Options;

import java.util.Collections;

public class Settings {
    private static boolean SOOT_INITIALIZED = false;
    private final static String androidJAR = "libs/android.jar";


    public static void initialiseSoot(String apkPath){
        if (SOOT_INITIALIZED)
            return;
        G.reset();

        Options.v().set_allow_phantom_refs(true);
//		Options.v().set_whole_program(true);
        Options.v().set_prepend_classpath(true);
        Options.v().set_validate(true);

        Options.v().set_output_format(Options.output_format_none);


        Options.v().set_process_dir(Collections.singletonList(apkPath));
        Options.v().set_force_android_jar(androidJAR);
        Options.v().set_src_prec(Options.src_prec_apk);
        Options.v().set_validate(true);
        Options.v().set_soot_classpath(androidJAR);

        Scene.v().loadNecessaryClasses();

        SOOT_INITIALIZED = true;
    }
}
