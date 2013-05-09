package test.util;

import java.io.File;

import org.junit.runners.model.InitializationError;

import com.xtremelabs.robolectric.RobolectricConfig;
import com.xtremelabs.robolectric.RobolectricTestRunner;

public class PathTestRunner extends RobolectricTestRunner{
    private static final String PROJECT_PATH = "../appagainsthumanity";
    
    public PathTestRunner(Class testClass) throws InitializationError {
        super(testClass, new RobolectricConfig(new File(PROJECT_PATH)));
        RobolectricConfig config = new RobolectricConfig(new File(PROJECT_PATH));
        //System.out.println(config.getSdkVersion());
    }

	public PathTestRunner(Class testClass, RobolectricConfig robolectricConfig,
			SQLiteMap sqLiteMap) throws InitializationError {
        super(testClass, new RobolectricConfig(new File(PROJECT_PATH)),sqLiteMap);
	}
}
