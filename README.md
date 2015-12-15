# More Emacs for NetBeans

* More Emacs is a NetBeans module for emacs users.
* NetBeans provides the emacs like key bindings.
 But emacs users require more emacs like key bindings.
 More emacs plugin provides more emacs like key bindings.
 The key bind scheme of more emacs is a child schema of Eclispe's emacs scheme.
 It adds some key bindings or overrides some bindings to emacs scheme.

## Install

* Not ready yet
* 

## Key Bindings

* The following list is key bindings of more emacs.
  * It is the difference from emacs bindings of NetBeans.
* 'rebind' means the rebind of NetBeans implementation.

|function|	binding|	description|
|:-----------|:------------|:------------|
|C-space|MoreEmacs set-mark|mark current posision|
|C-x C-x|MoreEmacs exchange-point-and-mark||
|C-w|MoreEmacs kill-region|cut region to clipboard|
|M-w|MoreEmacs kill-ring-save|copy region to clipboard|
|M-/|completion-show|rebind|
|C-M-/|all-completion-show|rebind|
|M-f|MoreEmacs forward-word||
|M-b|MoreEmacs backward-word||
|M-d|MoreEmacs kill-word||
|M-BS|MoreEmacs backward-kill-word||
|M-c|MoreEmacs capitalize-word||
|M-u|MoreEmacs upcase-word||
|M-l|MoreEmacs downcase-word||
|C-c C-c|MoreEmacs comment-region|toggles comment|
|C-i|MoreEmacs indent-line||
|tab|MoreEmacs indent-line||
|M-\\|MoreEmacs delete-horizontal-space||
|C-x r k|MoreEmacs kill-rectangle||
|C-x r y|MoreEmacs yank-rectangle||
|C-h|delete-previous|rebind|
|C-m|MoreEmacs new-line||
|C-j|insert-break|rebind|
|C-o|split-line|rebind|
|C-t|MoreEmacs transpose-chars|Sorry, the display may get corrupted.|
|M-t|MoreEmacs transpose-words||
|C-g|MoreEmacs keyboard-quit|send Escape key down/up events after 500ms.You should release ctrl key after C-g.|
|C-x 0|CloseWindowAction|rebind|
|C-x C-c|CloseAllDocumentsAction|rebind|
|C-l|adjust-window-center|rebind|


## Unicode

* The supplementary characters are supported. All characters are treated as code point.
* East Asian Width is supported. In the column calculation of rectangle operation, the width of ambiguous characters are 2 for CJK languages and 1 for other languages.


## Change Log

* 2015/12/12
  * Prepareing the firs release.
