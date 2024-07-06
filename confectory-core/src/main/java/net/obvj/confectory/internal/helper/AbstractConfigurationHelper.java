/*
 * Copyright 2024 obvj.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.obvj.confectory.internal.helper;

import net.obvj.confectory.ConfigurationException;

/**
 * An abstract Configuration Helper that retrieves contains common, pre-defined methods
 * for use with an abstract method that accepts a data type as a parameter.
 *
 * @param <T> the actual type
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 2.6.0
 */
public abstract class AbstractConfigurationHelper<T> implements ConfigurationHelper<T>
{

    /**
     * Returns the {@code Boolean} object associated with the specified path expression,
     * provided that the expression returns a single element that can be converted to
     * {@code boolean}.
     * <p>
     * <strong>Note:</strong> If the evaluation result is a valid string it will parsed
     * according to {@link Boolean#parseBoolean(String)}, it will return {@code false} for any
     * string different than {@code "true"} (ignoring case).
     *
     * @param path the path expression to evaluate (the actual expression type is defined by a
     *             concrete implementation)
     * @return the {@code Boolean} object from the evaluation result of the specified path
     *         expression; {@code null} if the expression is not found
     *
     * @throws NullPointerException   if the path expression is null
     * @throws ConfigurationException if the path expression is either invalid, or it
     *                                evaluates to more than a single element
     */
    @Override
    public Boolean getBoolean(String path)
    {
        return getValue(path, Boolean.class, false);
    }

    /**
     * Returns the {@code Boolean} object associated with the specified path expression,
     * provided that the expression returns a single element that can be converted to
     * {@code boolean}.
     * <p>
     * If no value is found for the given expression, then an exception will be thrown.
     * <p>
     * <strong>Note:</strong> If the evaluation result is a valid string it will parsed
     * according to {@link Boolean#parseBoolean(String)}, it will return {@code false} for any
     * string different than {@code "true"} (ignoring case).
     *
     * @param path the path expression to evaluate (the actual expression type is defined by a
     *             concrete implementation)
     * @return the {@code Boolean} object from the evaluation result of the the specified path
     *         expression; never {@code null}
     *
     * @throws NullPointerException   if the path expression is null
     * @throws ConfigurationException if the path expression is either invalid, not found, or
     *                                it evaluates to more than a single element
     */
    @Override
    public Boolean getMandatoryBoolean(String path)
    {
        return getValue(path, Boolean.class);
    }

    /**
     * Returns the {@code Integer} object associated with the specified path expression,
     * provided that the expression returns a single element that can be converted to
     * {@code int}.
     *
     * @param path the path expression to evaluate (the actual expression type is defined by a
     *             concrete implementation)
     * @return the {@code Integer} object from the evaluation result of the specified path
     *         expression; {@code null} if the expression is not found
     *
     * @throws NullPointerException   if the path expression is null
     * @throws ConfigurationException if the path expression is either invalid, or it
     *                                evaluates to more than a single element
     * @throws NumberFormatException  if the path result cannot be assigned to {@code int}
     */
    @Override
    public Integer getInteger(String path)
    {
        return getValue(path, Integer.class, false);
    }

    /**
     * Returns the {@code Integer} object associated with the specified path expression,
     * provided that the expression returns a single element that can be converted to
     * {@code int}.
     * <p>
     * If no value is found for the given expression, then an exception will be thrown.
     *
     * @param path the path expression to evaluate (the actual expression type is defined by a
     *             concrete implementation)
     * @return the {@code Integer} object from the evaluation result of the the specified path
     *         expression; never {@code null}
     *
     * @throws NullPointerException   if the path expression is null
     * @throws ConfigurationException if the path expression is either invalid, not found, or
     *                                it evaluates to more than a single element
     * @throws NumberFormatException  if the path result cannot be assigned to {@code int}
     */
    @Override
    public Integer getMandatoryInteger(String path)
    {
        return getValue(path, Integer.class);
    }

    /**
     * Returns the {@code Long} object associated with the specified path expression ,
     * provided that the expression returns a single element that can be converted to
     * {@code long}.
     *
     * @param path the path expression to evaluate (the actual expression type is defined by a
     *             concrete implementation)
     * @return the {@code Long} object from the evaluation result of the specified path
     *         expression; {@code null} if the expression is not found
     *
     * @throws NullPointerException   if the path expression is null
     * @throws ConfigurationException if the path expression is either invalid, or it
     *                                evaluates to more than a single element
     * @throws NumberFormatException  if the path result cannot be assigned to {@code long}
     */
    @Override
    public Long getLong(String path)
    {
        return getValue(path, Long.class, false);
    }

    /**
     * Returns the {@code Long} object associated with the specified path expression ,
     * provided that the expression returns a single element that can be converted to
     * {@code long}.
     * <p>
     * If no value is found for the given expression, then an exception will be thrown.
     *
     * @param path the path expression to evaluate (the actual expression type is defined by a
     *             concrete implementation)
     * @return the {@code Long} object from the evaluation result of the the specified path
     *         expression; never {@code null}
     *
     * @throws NullPointerException   if the path expression is null
     * @throws ConfigurationException if the path expression is either invalid, not found, or
     *                                it evaluates to more than a single element
     * @throws NumberFormatException  if the path result cannot be assigned to {@code long}
     */
    @Override
    public Long getMandatoryLong(String path)
    {
        return getValue(path, Long.class);
    }

    /**
     * Returns the {@code Double} object associated with the specified path expression,
     * provided that the expression returns a single element that can be converted to
     * {@code double}.
     *
     * @param path the path expression to evaluate (the actual expression type is defined by a
     *             concrete implementation)
     * @return the {@code Double} object from the evaluation result of the specified path
     *         expression; {@code null} if the expression is not found
     *
     * @throws NullPointerException   if the path expression is null
     * @throws ConfigurationException if the path expression is either invalid, or it
     *                                evaluates to more than a single element
     * @throws NumberFormatException  if the path result cannot be assigned to {@code double}
     */
    @Override
    public Double getDouble(String path)
    {
        return getValue(path, Double.class, false);
    }

    /**
     * Returns the {@code Double} object associated with the specified path expression,
     * provided that the expression returns a single element that can be converted to
     * {@code double}.
     * <p>
     * If no value is found for the given expression, then an exception will be thrown.
     *
     * @param path the path expression to evaluate (the actual expression type is defined by a
     *             concrete implementation)
     * @return the {@code Double} object from the evaluation result of the the specified path
     *         expression; never {@code null}
     *
     * @throws NullPointerException   if the path expression is null
     * @throws ConfigurationException if the path expression is either invalid, not found, or
     *                                it evaluates to more than a single element
     * @throws NumberFormatException  if the path result cannot be assigned to {@code double}
     */
    @Override
    public Double getMandatoryDouble(String path)
    {
        return getValue(path, Double.class);
    }

    /**
     * Returns the {@code String} object associated with the specified path expression,
     * provided that the expression returns a single element.
     *
     * @param path the path expression to evaluate (the actual expression type is defined by a
     *             concrete implementation)
     * @return the {@code String} value associated with the specified path expression;
     *         {@code null} if the expression is not found
     *
     * @throws NullPointerException   if the path expression is null
     * @throws ConfigurationException if the path expression is either invalid, or it
     *                                evaluates to more than a single element
     */
    @Override
    public String getString(String path)
    {
        return getValue(path, String.class, false);
    }

    /**
     * Returns the {@code String} object associated with the specified path expression,
     * provided that the expression returns a single element.
     * <p>
     * If no value is found for the given expression, then an exception will be thrown.
     *
     * @param path the path expression to evaluate (the actual expression type is defined by a
     *             concrete implementation)
     * @return the {@code String} value associated with the specified path expression; never
     *         {@code null}
     *
     * @throws NullPointerException   if the path expression is null
     * @throws ConfigurationException if the path expression is either invalid, not found, or
     *                                it evaluates to more than a single element
     */
    @Override
    public String getMandatoryString(String path)
    {
        return getValue(path, String.class);
    }

    /**
     * Returns the value associated with the specified path expression, provided that the
     * expression returns a single element that can be mapped to the specified class type.
     * <p>
     * If no value is found for the given expression, then an exception will be thrown.
     *
     * @param path       the path expression to evaluate (the actual expression type is
     *                   defined by a concrete implementation)
     * @param targetType the type the evaluation result should be converted to
     *
     * @return the object that is the result of evaluating the given path expression and
     *         converting the result to the specified {@code targetType}; never null
     *
     * @throws NullPointerException   if the path expression is null
     * @throws ConfigurationException if the path expression is either invalid, not found, or
     *                                it evaluates to more than a single element
     */
    protected <T> T getValue(String path, Class<T> targetType)
    {
        return getValue(path, targetType, true);
    }

    /**
     * Returns the value associated with the specified path expression in the XML document in
     * context, provided that the expression returns a single element that can be mapped to
     * the specified class type.
     * <p>
     * If no value is found for the given expression and the {@code mandatory} flag is
     * {@code true}, an exception will be thrown; if the flag is not set, then the method
     * returns {@code null}.
     *
     * @param path       the path expression to evaluate (the actual expression type is
     *                   defined by a concrete implementation)
     * @param targetType the type the evaluation result should be converted to
     * @param mandatory  a flag determining whether or not an exception should be raised in
     *                   case the expression returns no data
     *
     * @return the object that is the result of evaluating the given path expression and
     *         converting the result to the specified {@code targetType}; it may be
     *         {@code null} if no value is found and the {@code mandatory} flag is set
     *
     * @throws NullPointerException   if the path expression is null
     * @throws ConfigurationException if the path expression is either invalid, not found
     *                                (with the {@code mandatory} flag set), or it evaluates
     *                                to more than a single element
     */
    protected abstract <T> T getValue(String path, Class<T> targetType, boolean mandatory);

}
