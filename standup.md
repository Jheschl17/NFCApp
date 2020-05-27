# Weekly Standup

## 27.5.2020
completed work packages:
+ 4-create-project-scaffold
+ 23-create-tabbed-navigation
+ 2-create-scan-layout
+ 1-create-profiles-layout
+ 24-update-icons
+ 8-create-profile-detail-layout
+ 3-create-settings-layout

work packages for next week:
+ 18-implement-scan-interactivity
+ 6-implement-profiles-tab-interactivity
+ 10-implement-writing-NDEF
+ 11-implement-emulating-NDEF
+ 9-implement-reading-NDEF

Implementing work packages has so far not been an issue. Working on the App is quite productive.
There have have been some issues with the NFC protocols though. While NDEF (whichs spec would cost $200) is implemented in Android, other protocols like those specified in ISO/IEC 14443 (NFCa, NFCb) are not. Implementing those would require writing the protocol stack from scratch (hexdumps, yeah). As the specifications for the protocols cost 178 CHF and require a licencing fee, this could be quite a challenge.

For the next week working on the specified work packages will the goal. Since the other protocols are not required yet, this will not be a problem as of yet.
