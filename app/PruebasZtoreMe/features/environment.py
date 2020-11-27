from appium import webdriver

def before_all(context): 

	desired_cap = {
		"deviceName": "ce0616063b9e933f03",
  		"platformName": "Android",
  		"app": "C:\\Ubuntu\\ZtoreME.apk",
  		"autoGrantPermissions": "true"
	}

	context.driver = webdriver.Remote("http://localhost:4723/wd/hub", desired_cap)