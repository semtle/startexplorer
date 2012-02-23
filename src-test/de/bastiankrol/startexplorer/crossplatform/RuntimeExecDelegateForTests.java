package de.bastiankrol.startexplorer.crossplatform;

import java.io.File;
import java.io.IOException;

import de.bastiankrol.startexplorer.crossplatform.RuntimeExecDelegate;

class RuntimeExecDelegateForTests extends RuntimeExecDelegate
{

  RuntimeExecDelegateForTests()
  {
    super(true);
  }

  @Override
  public void exec(String execCommandString, File workingDirectory)
  {
    System.out.println("Executing: <" + execCommandString
        + ">, working directory: <" + workingDirectory + ">");
    try
    {
      this.getRuntime().exec(execCommandString, null, workingDirectory);
    }
    catch (IOException e)
    {
      StringBuilder builder = new StringBuilder();
      builder.append("The command could not be executed.");
      builder.append("\n");
      if (e.getMessage() != null)
      {
        builder.append(" Message: ");
        builder.append(e.getMessage());
        builder.append("\n");
      }
      System.err.println(builder.toString());
      e.printStackTrace();
    }
  }
}