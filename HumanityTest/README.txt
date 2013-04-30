To Run the Tests we either need to configure the Run Configuration for each File, so that in Arguments, Workspace the MainActivity Workspace is selected.

To work around this issue, in test.util there exists a TestRunner class that sets the path. 
Just use this to extend your testClasses from instead of Robolectric.