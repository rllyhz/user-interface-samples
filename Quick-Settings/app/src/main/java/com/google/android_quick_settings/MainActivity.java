// Copyright 2022 Google Inc.
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//      http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.android_quick_settings;

import android.app.StatusBarManager;
import android.content.ComponentName;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.drawable.IconCompat;
import com.google.common.util.concurrent.MoreExecutors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

  @RequiresApi(api = 33)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    StatusBarManager statusBarService = this.getSystemService(StatusBarManager.class);
    ComponentName componentName = new ComponentName(
        this.getApplicationContext(),
        QuickSettingsService.class.getName());
    AppCompatButton btn = findViewById(R.id.add_tile_btn);
    IconCompat icon =
        IconCompat.createWithResource(getApplicationContext(),
            android.R.drawable.ic_dialog_alert);

    btn.setOnClickListener(view -> {
      if (VERSION.CODENAME.equalsIgnoreCase("Tiramisu")) {
        statusBarService.requestAddTileService(
            componentName, "Quick Settings", icon.toIcon(MainActivity.this),
            MoreExecutors.directExecutor(), integer -> {
              setResult(integer);
              finish();
            });
      } else {
        Logger.getLogger(MainActivity.class.getName()).log(
            Level.INFO, "Request to add tile for user is not supported");
      }
    });
  }

  private String resultToString(int resultCode) {
    String value;
    switch(resultCode) {
      case StatusBarManager.TILE_ADD_REQUEST_RESULT_TILE_NOT_ADDED:
        value = "Tile not added";
        break;
      case StatusBarManager.TILE_ADD_REQUEST_RESULT_TILE_ALREADY_ADDED:
        value = "Tile already added";
        break;
      case StatusBarManager.TILE_ADD_REQUEST_RESULT_TILE_ADDED:
        value = "Tile added";
        break;
      case StatusBarManager.TILE_ADD_REQUEST_ERROR_MISMATCHED_PACKAGE:
        value = "Error: mismatched package";
        break;
      case StatusBarManager.TILE_ADD_REQUEST_ERROR_REQUEST_IN_PROGRESS:
        value = "Error: request in progress";
        break;
      case StatusBarManager.TILE_ADD_REQUEST_ERROR_BAD_COMPONENT:
        value = "Error: bad component name";
        break;
      case StatusBarManager.TILE_ADD_REQUEST_ERROR_NOT_CURRENT_USER:
        value = "Error: not current user";
        break;
      case StatusBarManager.TILE_ADD_REQUEST_ERROR_APP_NOT_IN_FOREGROUND:
        value = "Error: app not in foreground";
        break;
      case StatusBarManager.TILE_ADD_REQUEST_ERROR_NO_STATUS_BAR_SERVICE:
        value = "Error: no statusbar service found";
        break;
      default:
        value = "Unknown error";
    }
    return value;
  }
}
