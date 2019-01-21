package de.embl.cba.bigDataTools2.boundingBox;


/*
 * #%L
 * BigDataViewer core classes with minimal dependencies
 * %%
 * Copyright (C) 2012 - 2016 Tobias Pietzsch, Stephan Saalfeld, Stephan Preibisch,
 * Jean-Yves Tinevez, HongKee Moon, Johannes Schindelin, Curtis Rueden, John Bogovic
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

import bdv.util.BoundedValueDouble;

public class BoundedRealInterval
{
	private final BoundedValueDouble minValue;

	private final BoundedValueDouble maxValue;

	/**
	 * minimum realInterval size. {@code max - min >= minIntervalSize}.
	 */
	protected final double minIntervalSize;

	/**
	 * @param rangeMin
	 *            lower bound of the allowed range. the realInterval minimum cannot
	 *            be lower than this.
	 * @param rangeMax
	 *            upper bound of the allowed range. the realInterval maximum cannot
	 *            be lower than this.
	 * @param initialMin
	 *            initial realInterval minimum.
	 * @param initialMax
	 *            initial realInterval maximum.
	 * @param minIntervalSize
	 *            realInterval is forced to have this size at least, i.e., {@code max - min >= minIntervalSize}.
	 */
	public BoundedRealInterval( final double rangeMin, final double rangeMax, final double initialMin, final double initialMax, final double minIntervalSize )
	{
		this.minIntervalSize = minIntervalSize;

		minValue = new BoundedValueDouble( rangeMin, rangeMax - minIntervalSize, initialMin )
		{
			@Override
			public void setCurrentValue( final double value )
			{
				super.setCurrentValue( value );
				final double min = minValue.getCurrentValue();
				double max = maxValue.getCurrentValue();
				if ( min > max - minIntervalSize )
				{
					max = min + minIntervalSize;
					maxValue.setCurrentValue( max );
				}
				updateInterval( min, max );
			}
		};

		maxValue = new BoundedValueDouble( rangeMin + minIntervalSize, rangeMax, initialMax )
		{
			@Override
			public void setCurrentValue( final double value )
			{
				super.setCurrentValue( value );
				double min = minValue.getCurrentValue();
				final double max = maxValue.getCurrentValue();
				if ( min > max - minIntervalSize )
				{
					min = max - minIntervalSize;
					minValue.setCurrentValue( min );
				}
				updateInterval( min, max );
			}
		};
	}

	/**
	 * This is called when the {@link #getMinBoundedValue() min} or
	 * {@link #getMaxBoundedValue() max} values change. The default
	 * implementation does nothing but derived classes can use it to trigger UI
	 * updates, etc.
	 *
	 * @param min
	 *            the new minimum of the realInterval
	 * @param max
	 *            the new maximum of the realInterval
	 */
	protected void updateInterval( final double min, final double max )
	{}

	/**
	 * Get the current minimum of the realInterval.
	 *
	 * @return the current minimum of the realInterval.
	 */
	public BoundedValueDouble getMinBoundedValue()
	{
		return minValue;
	}

	/**
	 * Get the current maximum of the realInterval.
	 *
	 * @return the current maximum of the realInterval.
	 */
	public BoundedValueDouble getMaxBoundedValue()
	{
		return maxValue;
	}

	/**
	 * Get the current minimum of the allowed range. (The realInterval (
	 * {@link #getMinBoundedValue()}, {@link #getMaxBoundedValue()}) is be a
	 * non-empty realInterval within the allowed range.)
	 *
	 * @return the current minimum of the allowed range.
	 */
	public double getRangeMin()
	{
		return minValue.getRangeMin();
	}

	/**
	 * Get the current maximum of the allowed range. (The realInterval (
	 * {@link #getMinBoundedValue()}, {@link #getMaxBoundedValue()}) is be a
	 * non-empty realInterval within the allowed range.)
	 *
	 * @return the current maximum of the allowed range.
	 */
	public double getRangeMax()
	{
		return maxValue.getRangeMax();
	}

	/**
	 * Set the allowed range. The realInterval ({@link #getMinBoundedValue()},
	 * {@link #getMaxBoundedValue()}) is enforced to be a non-empty realInterval
	 * within the allowed range.
	 *
	 * @param min
	 *            minimum of the allowed range.
	 * @param max
	 *            maximum of the allowed range.
	 */
	public void setRange( final double min, final double max )
	{
		assert min < max - minIntervalSize;
		minValue.setRange( min, max - minIntervalSize );
		maxValue.setRange( min + minIntervalSize, max );
		final double currentMin = minValue.getCurrentValue();
		final double currentMax = maxValue.getCurrentValue();
		if ( currentMin > currentMax - minIntervalSize )
		{
			if ( currentMax == max )
				minValue.setCurrentValue( currentMax - minIntervalSize );
			else
				maxValue.setCurrentValue( currentMin + minIntervalSize );
		}
	}
}