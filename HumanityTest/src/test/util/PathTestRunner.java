package test.util;

import java.io.File;

import org.junit.runners.model.InitializationError;

import com.xtremelabs.robolectric.RobolectricConfig;
import com.xtremelabs.robolectric.RobolectricTestRunner;

public class PathTestRunner extends RobolectricTestRunner{
    private static final String PROJECT_PATH = "../appagainsthumanity";
    
    public PathTestRunner(Class testClass) throws InitializationError {
        super(testClass, new RobolectricConfig(new File(PROJECT_PATH)));
    }
}
