/*
 * Copyright 2000-2016 Vaadin Ltd.
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
package com.vaadin.data;

import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;

import com.vaadin.data.Binder.Binding;

/**
 * Status change handler for forms.
 * <p>
 * Register a handler using {@link Binder#setStatusHandler(BinderStatusHandler)}
 * to be able to customize the status change handling such as displaying
 * validation messages.
 * <p>
 * The list will contain results for either binding level or binder level, but
 * never both mixed. This is because binder level validation is not run if
 * binding level validation fails.
 *
 * @see Binder#setStatusHandler(BinderStatusHandler)
 * @see Binder#setStatusLabel(com.vaadin.ui.Label)
 * @see Binding#withStatusChangeHandler(StatusChangeHandler)
 *
 * @author Vaadin Ltd
 * @since 8.0
 *
 */
public interface BinderStatusHandler
        extends Consumer<List<BinderResult<?, ?>>>, Serializable {

}
