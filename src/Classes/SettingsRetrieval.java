package Classes;
import java.util.prefs.Preferences;
 public class SettingsRetrieval {

	public void saveParam(String PREF_NAME,String newValue)
	{
		// Retrieve the user preference node for the package com.mycompany
		Preferences prefs = Preferences.userNodeForPackage(getClass());
		prefs.put(PREF_NAME, newValue);

	}
	
	public String GetParam(String PREF_NAME)
	{
		Preferences prefs = Preferences.userNodeForPackage(getClass());
		String defaultValue = " ";
		String propertyValue = prefs.get(PREF_NAME, defaultValue); // "a string"
		return propertyValue ;
	}
}
