package de.bastiankrol.startexplorer.variables;

import static de.bastiankrol.startexplorer.variables.VariableManager.RESOURCE_EXTENSION_VAR;
import static de.bastiankrol.startexplorer.variables.VariableManager.RESOURCE_NAME_VAR;
import static de.bastiankrol.startexplorer.variables.VariableManager.RESOURCE_NAME_WIHTOUT_EXTENSION_VAR;
import static de.bastiankrol.startexplorer.variables.VariableManager.RESOURCE_PARENT_VAR;
import static de.bastiankrol.startexplorer.variables.VariableManager.RESOURCE_PATH_VAR;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import static org.mockito.Mockito.mock;

import java.io.File;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.variables.IStringVariableManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import de.bastiankrol.startexplorer.util.MessageDialogHelper;

public class VariablesManagerTest
{
  private VariableManager variableManager;

  @Mock
  private IStringVariableManager eclipseVariableManagerMock;

  @Mock
  private MessageDialogHelper messageDialogHelperMock;

  private String path;
  private String resourceName;
  private String extension;
  private File file;

  /**
   * JUnite before
   */
  @Before
  public void setUp()
  {
    MockitoAnnotations.initMocks(this);

    this.variableManager = new VariableManager(this.eclipseVariableManagerMock,
        this.messageDialogHelperMock);

    this.path = "/path/to/";
    this.extension = "txt";
    this.resourceName = "resource";
    this.file = new File(this.path + this.resourceName + "." + this.extension);

    // this.fileList = new ArrayList<File>();
    // this.fileList.add(new File("C:\\file\\to\\resource"));
    // this.fileList.add(new File("/file/to/another/resource"));
    // this.fileList
    // .add(new File(
    // "some weird string (RuntimeExecCalls doesn't check if it's a valid file"));
    //
    // this.runtimeExecCalls = new RuntimeExecCallsWindows();
    // this.runtimeExecCalls.setRuntimeExecDelegate(this.runtimeExecDelegateMock);
  }

  @Test
  public void testReplaceStartExplorerVariables() throws CoreException
  {
    String[] cmdArray = new String[] { "parent: " + RESOURCE_PARENT_VAR, //
        "name: " + RESOURCE_NAME_VAR, //
        "complete path: " + RESOURCE_PATH_VAR, //
        "name without extension: " + RESOURCE_NAME_WIHTOUT_EXTENSION_VAR, //
        "extension: " + RESOURCE_EXTENSION_VAR //
    };
    String[] expectedOutput = new String[] {//
    "parent: \"" + this.file.getParentFile().getAbsolutePath() + "\"", //
        "name: \"" + this.file.getName() + "\"", //
        "complete path: \"" + this.file.getAbsolutePath() + "\"", //
        "name without extension: \"" + this.resourceName +"\"", //
        "extension: \"" + this.extension +"\""//
    };

    when(this.eclipseVariableManagerMock.performStringSubstitution(anyString()))
        .thenAnswer(new Answer<String>()
        {
          @Override
          public String answer(InvocationOnMock invocation)
          {
            return (String) invocation.getArguments()[0];
          }
        });

    this.variableManager.replaceAllVariablesInCommand(cmdArray, this.file,
        true, false);

    assertThat(cmdArray.length, is(equalTo(expectedOutput.length)));
    for (int i = 0; i < cmdArray.length; i++)
    {
      assertThat(cmdArray[i], is(equalTo(expectedOutput[i])));
    }
  }
}
