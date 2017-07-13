/*
 * Copyright (c) 2015, Yasuhiro Endoh
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *   * Neither the name of the authors nor the names of its contributors may be
 *     used to endorse or promote products derived from this software without
 *     specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package io.github.yas99en.moreemacsnb.core.actions;

import java.awt.event.ActionEvent;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Caret;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.EditorActionRegistration;

/**
 *
 * @author Yasuhiro Endoh
 */
@EditorActionRegistration(name="io-github-yas99en-moreemacsnb-core-actions-SetMarkAction")
public class SetMarkAction extends MoreEmacsAction {

    private static class MarkMoveListener implements CaretListener {

        private static class DocumentEditListener implements DocumentListener {

            private final MarkMoveListener myOwner;

            public DocumentEditListener(MarkMoveListener owner) {
                myOwner = owner;
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                myOwner.stopListening();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                myOwner.stopListening();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                myOwner.stopListening();
            }
        }

        private JTextComponent activeOn = null;
        private boolean isActive = false;
        private int lastMoveToPosition = -1;
        private int lastMoveDirection = 0;
        private boolean toggleLastMoveDirection = false;
        private final DocumentEditListener myEditListener = new DocumentEditListener(this);

        @Override
        public void caretUpdate(CaretEvent e) {
            int start = Mark.get(activeOn);
            int current;
            if (!isActive) {
                if (e.getDot() == lastMoveToPosition) {
                    // idempotent event, we'll have to move +/- 1
                    current = e.getDot() + lastMoveDirection;
                } else if (e.getDot() == start) {
                    // pressed the left or right button to go back, netbeans
                    // tries to reset the selection, we cannot have that.
                    current = lastMoveToPosition - lastMoveDirection;
                    toggleLastMoveDirection = true;
                } else {
                    current = e.getDot();
                }
                isActive = true;
                SwingUtilities.invokeLater(() -> {
                    activeOn.setCaretPosition(start);
                    activeOn.moveCaretPosition(current);
                    if (lastMoveToPosition < current) {
                        lastMoveDirection = 1;
                    } else if (lastMoveToPosition > current) {
                        lastMoveDirection = -1;
                    }
                    if (toggleLastMoveDirection) {
                        lastMoveDirection *= -1;
                        toggleLastMoveDirection = false;
                    }
                    lastMoveToPosition = current;
                    SwingUtilities.invokeLater(() -> {
                        isActive = false;
                    });
                });
            }
        }

        private void listenTo(JTextComponent target) {
            lastMoveToPosition = -1;
            if (activeOn == target) {
                stopListening();
            } else {
                if (activeOn != null) {
                    stopListening();
                }
                activeOn = target;
                target.addCaretListener(this);
                target.getDocument().addDocumentListener(myEditListener);
            }
        }

        private void stopListening() {
            // second activation on this target, stop marking
            JTextComponent target = activeOn;
            target.removeCaretListener(this);
            target.getDocument().removeDocumentListener(myEditListener);
            int dot = target.getCaretPosition();
            SwingUtilities.invokeLater(() -> {
                target.select(dot, dot);
            });
            activeOn = null;
        }
    }

    private static final MarkMoveListener LISTENER = new MarkMoveListener();

    public SetMarkAction() {
        super("set-mark");
    }

    @Override
    public void actionPerformed(ActionEvent e, JTextComponent target) {
        Caret caret = target.getCaret();
        Mark.set(target, caret.getDot());
        LISTENER.listenTo(target);
    }

}
