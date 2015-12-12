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

The following list is key bindings of more emacs. It is based on the emacs bindings of NetBeans.

|function|	binding|	description|
|:-----------|:------------|:------------|
|C-x 0|CloseWindowAction|rebind|
|C-x C-c|CloseAllDocumentsAction|rebind|
|C-g|MoreEmacs keyboard-quit| not work well|
|C-l|adjust-window-center|rebind|
|M-/|completion-show|rebind|
|C-M-/|all-completion-show|rebind|
|M-f|||
|M-b|||
|M-BS|||
|M-d|||
|C-C C-C|||
|C-w|||
|M-W|||
|C-h|delete-previous|rebind|
|C-j|insert-break|rebind|
|M-u|||
|M-l|||
|C-i|||
|tab|||
|C-o|split-line|rebind|
|C-space|MoreEmacs set-mark|mark current posision|





## Unicode

* The supplementary characters are supported. All characters are treated as code point.
* East Asian Width is supported. In the column calculation of rectangle operation, the width of ambiguous characters are 2 for CJK languages and 1 for other languages.


## Change Log

* 2015/12/12
  * Prepareing the firs release.
