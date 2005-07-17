/*
 * Copyright 2001-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.apache.jmeter.functions;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.chain.Context;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.protocol.java.sampler.ChainSampler;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;

/**
 * Delegates the call to the Iterator 
 * stored under the given target variable 
 */
public class HasNextNode extends AbstractFunction implements Serializable
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3834589898497012021L;

    private static final String KEY = "__hasNextNode";

    private static final List desc = new LinkedList();

    static
    {
        desc.add("variable");
    }

    private Object[] values;

    public String execute(SampleResult arg0, Sampler arg1)
            throws InvalidVariableException
    {
        JMeterContext ctx = JMeterContextService.getContext();
        Context chainCtx = (Context) ctx.getVariables().getObject(
            ChainSampler.CHAINS_CONTEXT);
        CompoundVariable compVar = (CompoundVariable) values[0] ; 
        String var = compVar.getRawParameters();
        Iterator iter = (Iterator) chainCtx.get(var) ;
        return new Boolean(iter.hasNext()).toString() ;
    }

    public List getArgumentDesc()
    {
        return desc;
    }

    public String getReferenceKey()
    {
        return KEY;
    }

    public void setParameters(Collection parameters)
            throws InvalidVariableException
    {
        values = parameters.toArray();
        if (values.length != 1)
        {
            throw new InvalidVariableException("it only accepts on parameter");
        }
    }

}
