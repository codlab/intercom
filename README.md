# Intercom

An Android library that provides a simple Sender / Receiver implementation for Android Wear

This library is licensed under the Apache 2.0 License. Check out the LICENSE.txt file for more information.


# Features

- implements a simple sender Class
- implements a simple receiver Class

# How to install this project
This project is available on jitpack. To use the project on Android Studio, make sure you have the following in your project's build.gradle file :
```
repositories {
	    maven { url "https://jitpack.io" }
	}
```
and compile the project in your module's build.gradle :
```
dependencies {
	        compile 'com.github.codlab:intercom:0.2.1'
	}
```

# Using the library


## Receiver

The receiver part must extends the AbstractWearListenerService service.

```
public class MobileKeyListenerService extends AbstractWearListenerService {

    public MobileKeyListenerService() {
        super();
    }
    
    @Override
    public boolean isManaged(@NonNull String path) {
      return "some string".equals(path);
    }

    @Override
    public void onNewMessage(@NonNull String path, @NonNull Bundle bundle) {
      //manage the result here
      //- start a service
      //- broadcast a message
      //- ...
    }
}
```

In the manifest, the class must be registered

```xml
  <service android:name=".service.MobileKeyListenerService">
      <intent-filter>
          <action android:name="com.google.android.gms.wearable.BIND_LISTENER" />
      </intent-filter>

      <intent-filter>
          <!-- listeners receive events that match the action and data filters -->
          <action android:name="com.google.android.gms.wearable.DATA_CHANGED" />
          <action android:name="com.google.android.gms.wearable.MESSAGE_RECEIVED" />
          <action android:name="com.google.android.gms.wearable.CAPABILITY_CHANGED" />
          <action android:name="com.google.android.gms.wearable.CHANNEL_EVENT" />

          <data
              android:host="*"
              android:pathPrefix="@string/some_string"
              android:scheme="wear" />
            </intent-filter>
        </service>
```

the android:pathPrefix sera par exemple égale à "some string" dans la class précédente, etc...


## Sender

The WearSenderObject is used to send messages. You must pass a valid context to the object 
```
WearSenderObject wear_sender_object = new WearSenderObject(some_context);
```


```
Bundle bundle = new Bundle();
...

wear_sender_object.sendMessage("some_path", bundle);
```

And stop it when the lifecycle ends (  :'( )

```
wear_sender_object.stop();
```

# Licence

   Copyright 2016 Kevin Le Perf @Codlab

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
