/**
 * Copyright (c) 2022 aoshiguchen
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package fun.asgc.neutrino.core.util;

import java.util.function.Supplier;

/**
 * 函数工具
 * @author: aoshiguchen
 * @date: 2022/7/1
 */
public class FunctionUtil {

	/**
	 * 获取第一个非空值
	 * @param suppliers
	 * @param <T>
	 * @return
	 */
	public static <T> T getFirstNotNull(Supplier<T> ...suppliers) {
		T res;
		if (ArrayUtil.notEmpty(suppliers)) {
			for (Supplier<T> supplier : suppliers) {
				if (null != supplier) {
					res = supplier.get();
					if (null != res) {
						return res;
					}
				}
			}
		}
		return null;
	}

}