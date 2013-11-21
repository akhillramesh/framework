/*
 * Copyright 2000-2013 Vaadin Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.vaadin.client.ui.grid;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * An element-like bundle representing a configurable and visual scrollbar in
 * one axis.
 * 
 * @since 7.2
 * @author Vaadin Ltd
 * @see VerticalScrollbarBundle
 * @see HorizontalScrollbarBundle
 */
abstract class ScrollbarBundle {
    private static final String CLASS_NAME = Escalator.CLASS_NAME + "-scroller";

    /**
     * The pixel size for OSX's invisible scrollbars.
     * <p>
     * Touch devices don't show a scrollbar at all, so the scrollbar size is
     * irrelevant in their case. There doesn't seem to be any other popular
     * platforms that has scrollbars similar to OSX. Thus, this behavior is
     * tailored for OSX only, until additional platforms start behaving this
     * way.
     */
    private static final int OSX_INVISIBLE_SCROLLBAR_FAKE_SIZE_PX = 13;

    /**
     * A representation of a single vertical scrollbar.
     * 
     * @see VerticalScrollbarBundle#getElement()
     */
    final static class VerticalScrollbarBundle extends ScrollbarBundle {
        public VerticalScrollbarBundle() {
            root.addClassName(CLASS_NAME + "-vertical");
        }

        @Override
        public void setScrollPos(int px) {
            root.setScrollTop(px);
        }

        @Override
        public int getScrollPos() {
            return root.getScrollTop();
        }

        @Override
        protected void internalSetScrollSize(int px) {
            scrollSizeElement.getStyle().setHeight(px, Unit.PX);
        }

        @Override
        public int getScrollSize() {
            return scrollSizeElement.getOffsetHeight();
        }

        @Override
        protected void internalSetOffsetSize(int px) {
            root.getStyle().setHeight(px, Unit.PX);
        }

        @Override
        public int getOffsetSize() {
            return root.getOffsetHeight();
        }

        @Override
        protected void internalSetScrollbarThickness(int px) {
            root.getStyle().setWidth(px, Unit.PX);
            scrollSizeElement.getStyle().setWidth(px, Unit.PX);
        }

        @Override
        protected int internalGetScrollbarThickness() {
            return root.getOffsetWidth();
        }

        @Override
        protected void forceScrollbar(boolean enable) {
            if (enable) {
                root.getStyle().setOverflowY(Overflow.SCROLL);
            } else {
                root.getStyle().clearOverflowY();
            }
        }
    }

    /**
     * A representation of a single horizontal scrollbar.
     * 
     * @see HorizontalScrollbarBundle#getElement()
     */
    final static class HorizontalScrollbarBundle extends ScrollbarBundle {
        protected HorizontalScrollbarBundle() {
            root.addClassName(CLASS_NAME + "-horizontal");
        }

        @Override
        public void setScrollPos(int px) {
            root.setScrollLeft(px);
        }

        @Override
        public int getScrollPos() {
            return root.getScrollLeft();
        }

        @Override
        protected void internalSetScrollSize(int px) {
            scrollSizeElement.getStyle().setWidth(px, Unit.PX);
        }

        @Override
        public int getScrollSize() {
            return scrollSizeElement.getOffsetWidth();
        }

        @Override
        protected void internalSetOffsetSize(int px) {
            root.getStyle().setWidth(px, Unit.PX);
        }

        @Override
        public int getOffsetSize() {
            return root.getOffsetWidth();
        }

        @Override
        protected void internalSetScrollbarThickness(int px) {
            root.getStyle().setHeight(px, Unit.PX);
            scrollSizeElement.getStyle().setHeight(px, Unit.PX);
        }

        @Override
        protected int internalGetScrollbarThickness() {
            return root.getOffsetHeight();
        }

        @Override
        protected void forceScrollbar(boolean enable) {
            if (enable) {
                root.getStyle().setOverflowX(Overflow.SCROLL);
            } else {
                root.getStyle().clearOverflowX();
            }
        }
    }

    protected final Element root = DOM.createDiv();
    protected final Element scrollSizeElement = DOM.createDiv();
    protected boolean isInvisibleScrollbar = false;

    private ScrollbarBundle() {
        root.appendChild(scrollSizeElement);
        root.setClassName(CLASS_NAME);
    }

    /**
     * Gets the root element of this scrollbar-composition.
     * 
     * @return the root element
     */
    public Element getElement() {
        return root;
    }

    /**
     * Modifies the scroll position of this scrollbar by a number of pixels
     * 
     * @param delta
     *            the delta in pixels to change the scroll position by
     */
    public void setScrollPosByDelta(int delta) {
        if (delta != 0) {
            /*
             * TODO [[optimize]]: rewrite this so that we can evoid a potential
             * reflow from getScrollPos()
             */
            setScrollPos(getScrollPos() + delta);
        }
    }

    /**
     * Modifies {@link #root root's} dimensions in the axis the scrollbar is
     * representing.
     * 
     * @param px
     *            the new size of {@link #root} in the dimension this scrollbar
     *            is representing
     */
    protected abstract void internalSetOffsetSize(int px);

    /**
     * Sets the length of the scrollbar.
     * 
     * @param px
     *            the length of the scrollbar in pixels
     */
    public void setOffsetSize(int px) {
        internalSetOffsetSize(px);
        forceScrollbar(needsScrollbars());
    }

    /**
     * Force the scrollbar to be visible with CSS. In practice, this means to
     * set either <code>overflow-x</code> or <code>overflow-y</code> to "
     * <code>scroll</code>" in the scrollbar's direction.
     * <p>
     * This is an IE8 workaround, since it doesn't always show scrollbars with
     * <code>overflow: auto</code> enabled.
     */
    protected abstract void forceScrollbar(boolean enable);

    /**
     * Gets the length of the scrollbar
     * 
     * @return the length of the scrollbar in pixels
     */
    public abstract int getOffsetSize();

    /**
     * Sets the scroll position of the scrollbar in the axis the scrollbar is
     * representing.
     * 
     * @param px
     *            the new scroll position in pixels
     */
    public abstract void setScrollPos(int px);

    /**
     * Gets the scroll position of the scrollbar in the axis the scrollbar is
     * representing.
     * 
     * @return the new scroll position in pixels
     */
    public abstract int getScrollPos();

    /**
     * Modifies {@link #scrollSizeElement scrollSizeElement's} dimensions in
     * such a way that the scrollbar is able to scroll a certain number of
     * pixels in the axis it is representing.
     * 
     * @param px
     *            the new size of {@link #scrollSizeElement} in the dimension
     *            this scrollbar is representing
     */
    protected abstract void internalSetScrollSize(int px);

    /**
     * Sets the amount of pixels the scrollbar needs to be able to scroll
     * through.
     * 
     * @param px
     *            the number of pixels the scrollbar should be able to scroll
     *            through
     */
    public void setScrollSize(int px) {
        internalSetScrollSize(px);
        forceScrollbar(needsScrollbars());
    }

    /**
     * Gets the amount of pixels the scrollbar needs to be able to scroll
     * through.
     * 
     * @return the number of pixels the scrollbar should be able to scroll
     *         through
     */
    public abstract int getScrollSize();

    /**
     * Modifies {@link #scrollSizeElement scrollSizeElement's} dimensions in the
     * opposite axis to what the scrollbar is representing.
     * 
     * @param px
     *            the dimension that {@link #scrollSizeElement} should take in
     *            the opposite axis to what the scrollbar is representing
     */
    protected abstract void internalSetScrollbarThickness(int px);

    /**
     * Sets the scrollbar's thickness.
     * <p>
     * If the thickness is set to 0, the scrollbar will be treated as an
     * "invisible" scrollbar. This means, the DOM structure will be given a
     * non-zero size, but {@link #getScrollbarThickness()} will still return the
     * value 0.
     * 
     * @param px
     *            the scrollbar's thickness in pixels
     */
    public void setScrollbarThickness(int px) {
        isInvisibleScrollbar = (px == 0);
        internalSetScrollbarThickness(px != 0 ? px
                : OSX_INVISIBLE_SCROLLBAR_FAKE_SIZE_PX);
    }

    /**
     * Gets the scrollbar's thickness as defined in the DOM.
     * 
     * @return the scrollbar's thickness as defined in the DOM, in pixels
     */
    protected abstract int internalGetScrollbarThickness();

    /**
     * Gets the scrollbar's thickness.
     * <p>
     * This value will differ from the value in the DOM, if the thickness was
     * set to 0 with {@link #setScrollbarThickness(int)}, as the scrollbar is
     * then treated as "invisible."
     * 
     * @return the scrollbar's thickness in pixels
     */
    public int getScrollbarThickness() {
        if (!isInvisibleScrollbar) {
            return internalGetScrollbarThickness();
        } else {
            return 0;
        }
    }

    /**
     * Checks whether the scrollbar should be active and needed, i.e. if the
     * "content" is larger than the "frame".
     * 
     * @return <code>true</code> iff the scrollbar should be active and needed
     */
    protected boolean needsScrollbars() {
        return getOffsetSize() < getScrollSize();
    }
}
