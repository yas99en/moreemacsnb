# More Emacs for NetBeans

* More Emacs is a NetBeans module for emacs users.
* NetBeans provides the emacs like key bindings.
 But emacs users require more emacs like key bindings.
 More emacs plugin provides more emacs like key bindings.

## Install

* JDK 1.8 or higher required.
    * Only for mac user: see [**the settings for meta key**](#meta-key-on-mac).
* For NetBeans 8.1, it can be installed from the plugin manager of IDE.
    * Open "Tools/Plugins" dialog.
    * Open "Available Plugins" tab, select "MoreEmacs", then press "Install" button.
    * Open "Tools/Options" dialog, and open Keymap tab, select "MoreEmacs".

### Manual Install

Of cause, you can install manually.
* Download the nbm file from [NetBeans Plugin Portal](http://plugins.netbeans.org/plugin/62006/moreemacs).
* Open Tools/Plugins dialog.
* Open Downloaded tab, press "Add Plugin...", select the nbm file, press "Install".
* Open "Tools/Options" dialog, and open Keymap tab, select "MoreEmacs".

<img src="moreemacsnb.png" width="300">

## Key Bindings

* The following lists are major key bindings of more emacs.
    * 'rebind' means the rebind of NetBeans implementation.
    * 'nb emacs' means the binding of NetBeans Emacs bidings.

### Editor Bindings

* [The complete editor bindings definition of MoreEmacs.](https://github.com/yas99en/moreemacsnb/blob/master/core/src/io/github/yas99en/moreemacsnb/core/actions/MoreEmacs-keybindings.xml)
    * [modifier symbols](http://bits.netbeans.org/8.0/javadoc/org-openide-util/org/openide/util/Utilities.html#stringToKey(java.lang.String))
* For mac user, both option key and command key are assigned to meta key.

|function|	binding|	description|
|:-----------|:------------|:------------|
|C-space|MoreEmacs set-mark|mark current posision|
|C-x C-x|MoreEmacs exchange-point-and-mark||
|C-w|MoreEmacs kill-region|cut region to clipboard and kill ring|
|M-w|MoreEmacs kill-ring-save|copy region to clipboard and kill ring|
|C-y|MoreEmacs yank|paste from kill ring|
|M-y|MoreEmacs yank-again|cycle through kill ring|
|C-x u|undo|nb emacs|
|C-S-MINUS|undo|nb emacs|
|C-MINUS|redo||
|C-/|comment-region|nb emacs|
|M-/|completion-show|rebind|
|S-M-/|all-completion-show|rebind|
|C-M-/|all-completion-show|rebind|
|C-f|caret-forward|nb emacs|
|C-b|caret-backward|nb emacs|
|MS-,|caret-begin|nb emacs|
|MS-.|caret-begin|nb emacs|
|C-a|caret-line-first-column|nb emacs|
|C-e|caret-begin|nb emacs|
|C-p|caret-up|nb emacs|
|C-n|caret-down|nb emacs|
|C-v|page-down|nb emacs|
|M-v|page-up|nb emacs|
|C-l|adjust-window-center|rebind|
|C-k|cut-to-line-end|nb emacs|
|C-d|delete-next|nb emacs|
|DEL|delete-next|nb emacs|
|C-h|delete-previous|rebind|
|BS|delete-previous|rebind|
|M-\\|MoreEmacs delete-horizontal-space||
|M-f|MoreEmacs forward-word||
|M-b|MoreEmacs backward-word||
|M-d|MoreEmacs kill-word||
|M-BS|MoreEmacs backward-kill-word||
|M-c|MoreEmacs capitalize-word||
|M-u|MoreEmacs upcase-word||
|M-l|MoreEmacs downcase-word||
|C-c C-c|MoreEmacs toggle-comment-region|toggles comment|
|C-i|MoreEmacs indent-line||
|TAB|MoreEmacs indent-line||
|C-q TAB|insert-tab|nb emacs|
|C-j|insert-break|rebind|
|C-m|MoreEmacs new-line||
|C-o|split-line|rebind|
|C-x r k|MoreEmacs kill-rectangle||
|C-x r y|MoreEmacs yank-rectangle||
|C-x r t|MoreEmacs insert-rectangle-string||
|C-x SPACE|toggle-rectangular-selection||
|C-s|find|nb emacs|
|C-r|find|rebind|
|M-s|find-next|nb emacs|
|M-r|find-previous|nb emacs|
|MS-5|replace|nb emacs|
|C-g|MoreEmacs keyboard-quit|dispatch escape key down/up events internally.|
|C-t|MoreEmacs transpose-chars|Sorry, the display may get corrupted.|
|M-t|MoreEmacs transpose-words||
|MS-f|fix-imports|nb emacs|
|M-S-r|in-place-refactoring|rebind|

### Menu Bindings

* [The complete menu bindings definition of MoreEmacs.](https://github.com/yas99en/moreemacsnb/blob/master/core/src/io/github/yas99en/moreemacsnb/core/layer.xml)
    * [modifier symbols](http://bits.netbeans.org/8.0/javadoc/org-openide-util/org/openide/util/Utilities.html#stringToKey(java.lang.String))

|function|	binding|	description|
|:-----------|:------------|:------------|
|C-x C-s|SaveAction|nb emacs|
|C-x C-f|OpenFileAction|nb emacs|
|C-x 1|MaximizeWindowAction|nb emacs|
|C-x k|CloseWindowAction|nb emacs|
|C-x 0|CloseWindowAction|rebind|
|C-x C-c|CloseAllDocumentsAction|rebind. This is my preference.|

## Unicode

* The supplementary characters are supported. All characters are treated as code point.
* East Asian Width is supported. In the column calculation of rectangle operation, the width of ambiguous characters are 2 for CJK languages and 1 for other languages.

## Meta Key on Mac

* MoreEmacs assigns both commnad key and option key to meta key in the editor bindings.
* To use option key as meta key, the following settings are required.
* Edit NetBeans configuration file as follows:
    * Open `/Applications/NetBeans/NetBeans `***NBVERSION***`.app/Contents/Resources/NetBeans/etc/netbeans.conf` by text editor.
    * Add `-J-Dnetbeans.editor.keymap.compatible=true` at end of value of `netbeans_default_options` parameter. (see following diff result)
    * Restart NetBeans.

```    
> diff -U0 "/Applications/NetBeans/NetBeans 8.1.app/Contents/Resources/NetBeans/etc/netbeans.conf"{.orig,}
--- /Applications/NetBeans/NetBeans 8.1.app/Contents/Resources/NetBeans/etc/netbeans.conf.orig	2015-12-19 10:21:02.000000000 +0900
+++ /Applications/NetBeans/NetBeans 8.1.app/Contents/Resources/NetBeans/etc/netbeans.conf	2015-12-20 16:58:06.000000000 +0900
@@ -46 +46 @@
-netbeans_default_options="-J-client -J-Xss2m -J-Xms32m -J-Dapple.laf.useScreenMenuBar=true -J-Dapple.awt.graphics.UseQuartz=true -J-Dsun.java2d.noddraw=true -J-Dsun.java2d.dpiaware=true -J-Dsun.zip.disableMemoryMapping=true"
+netbeans_default_options="-J-client -J-Xss2m -J-Xms32m -J-Dapple.laf.useScreenMenuBar=true -J-Dapple.awt.graphics.UseQuartz=true -J-Dsun.java2d.noddraw=true -J-Dsun.java2d.dpiaware=true -J-Dsun.zip.disableMemoryMapping=true -J-Dnetbeans.editor.keymap.compatible=true"
```


## Change Log

* 2015/12/18
  * v1.0.0 release
* 2015/12/15
  * changed C-r to find
  * added M-S-r as in-place-refactoring
* 2015/12/12
  * Prepareing the first release.

## Links

* [NetBeans Plugin Portal](http://plugins.netbeans.org/)
* [MoreEmacs on NetBeans Plugin Portal](http://plugins.netbeans.org/plugin/62006/moreemacs)
* [NetBeans Bug 99443 - support emacs set-mark-command (C-Space, C-@)](https://netbeans.org/bugzilla/show_bug.cgi?id=99443)
