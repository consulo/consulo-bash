/*
 * Copyright 2013 Joachim Ansorg, mail@ansorg-it.com
 * File: MockPsiBuilder.java, Class: MockPsiBuilder
 * Last modified: 2013-04-29
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ansorgit.plugins.bash.lang.parser;

import com.ansorgit.plugins.bash.lang.lexer.BashTokenTypes;
import com.google.common.collect.Lists;
import consulo.language.ast.ASTNode;
import consulo.language.ast.TokenSet;
import consulo.language.parser.ITokenTypeRemapper;
import consulo.language.parser.PsiBuilder;
import consulo.language.parser.WhitespaceSkippedCallback;
import consulo.logging.Logger;
import consulo.project.Project;
import consulo.util.lang.Pair;
import consulo.language.ast.IElementType;
import consulo.util.collection.Stack;
import consulo.language.util.FlyweightCapableTreeStructure;
import consulo.language.ast.LighterASTNode;
import consulo.language.parser.WhitespacesAndCommentsBinder;
import consulo.localize.LocalizeValue;
import consulo.util.dataholder.UnprotectedUserDataHolder;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Date: 24.03.2009
 * Time: 21:58:36
 *
 * @author Joachim Ansorg
 */
public class MockPsiBuilder extends UnprotectedUserDataHolder implements PsiBuilder
{
	private static final Logger log = Logger.getInstance("#bash.MockPsiBuilder");
	private List<IElementType> elements;
	private List<String> textTokens;
	private List<String> errors = new ArrayList<String>();
	private Stack<MockMarker> markers = new Stack<MockMarker>();

	private List<Pair<MockMarker, IElementType>> doneMarkers = Lists.newLinkedList();

	private static final TokenSet ignoredTokens = TokenSet.orSet(BashTokenTypes.whitespaceTokens);
	private TokenSet enforcedCommentTokens = BashTokenTypes.commentTokens;

	int elementPosition = 0;
	private ITokenTypeRemapper tokenRemapper = null;

	public MockPsiBuilder(IElementType... data)
	{
		this.elements = new ArrayList<IElementType>();
		this.elements.addAll(Arrays.asList(data));

		this.textTokens = Lists.newLinkedList();
	}

	public MockPsiBuilder(List<String> textTokens, IElementType... data)
	{
		this.elements = new ArrayList<IElementType>();
		this.elements.addAll(Arrays.asList(data));

		this.textTokens = Lists.newLinkedList(textTokens);
	}

	public boolean hasErrors()
	{
		return errors.size() > 0;
	}

	public List<String> getErrors()
	{
		return errors;
	}

	public int processedElements()
	{
		return elementPosition;
	}

	public Project getProject()
	{
		return null;
	}

	public CharSequence getOriginalText()
	{
		return "unkown (mocked PsiBuilder)";
	}

	public void advanceLexer()
	{
		elementPosition++;
	}

	public IElementType getTokenType()
	{
		if(elementPosition < elements.size())
		{
			IElementType type = elements.get(elementPosition);
			while(enforcedCommentTokens.contains(type) || ignoredTokens.contains(type))
			{
				if(elementPosition + 1 >= elements.size())
				{
					return null;
				}

				advanceLexer();
				type = elements.get(elementPosition);
			}

			return (tokenRemapper != null) ? tokenRemapper.filter(type, elementPosition, elementPosition + 1, null) : type;
		}

		return null;
	}

	public void setTokenTypeRemapper(ITokenTypeRemapper iTokenTypeRemapper)
	{
		this.tokenRemapper = iTokenTypeRemapper;
	}

	public String getTokenText()
	{
		getTokenType();//find the right one
		return textTokens.size() > elementPosition ? textTokens.get(elementPosition) : "unkown";
	}


	public void remapCurrentToken(IElementType newTokenType)
	{
		//position the current element pointer
		getTokenType();
		elements.set(elementPosition, newTokenType);
	}

	public void setWhitespaceSkippedCallback(WhitespaceSkippedCallback whitespaceSkippedCallback)
	{
		//fixme
		throw new UnsupportedOperationException();
	}

	public IElementType lookAhead(int lookAhead)
	{
		int indexPos = elementPosition;
		int elementCounter = 0;
		int length = elements.size();

		while(indexPos < length && elementCounter < lookAhead)
		{
			indexPos++;

			while(indexPos < length && elements.get(indexPos) == BashTokenTypes.WHITESPACE)
			{
				indexPos++;
			}

			elementCounter++;
		}

		return indexPos < length ? elements.get(indexPos) : null;
	}

	public IElementType rawLookup(int lookAhead)
	{
		return elementPosition + lookAhead < elements.size() ? elements.get(elementPosition + lookAhead) : null;
	}

	public int rawTokenTypeStart(int lookAhead)
	{
		int requestedPos = elementPosition + lookAhead;
		if(requestedPos >= textTokens.size())
		{
			log.error("Invalid request for rawTokenTypeStart!");
			return -1;
		}

		if(requestedPos == 0)
		{
			return -1;
		}

		int offset = 0;
		for(int i = 0; i <= requestedPos; ++i)
		{
			offset += textTokens.get(i).length();
		}

		return offset;
	}

	@Override
	public int rawTokenIndex()
	{
		return 0;
	}

	public int getCurrentOffset()
	{
		return elementPosition;
	}

	public Marker mark()
	{
		//store the stacktrace for better debugging
		StringBuilder details = new StringBuilder("Marker opened at:\n");
		try
		{
			throw new IllegalStateException();
		}
		catch(IllegalStateException e)
		{
			final StackTraceElement[] stack = e.getStackTrace();
			int length = stack.length;
			for(int i = 0; i < length && i < 10; ++i)
			{
				details.append(stack[i].toString()).append("\n");
			}
		}

		final MockMarker mockMarker = new MockMarker(elementPosition, details.toString());
		markers.push(mockMarker);
		return mockMarker;
	}

	public void error(LocalizeValue s)
	{
		errors.add("[" + elementPosition + "]: " + s);
	}

	public boolean eof()
	{
		return elementPosition >= elements.size();
	}

	public ASTNode getTreeBuilt()
	{
		//fixme
		return null;
	}

	public FlyweightCapableTreeStructure<LighterASTNode> getLightTree()
	{
		//fixme
		return null;
	}

	public void setDebugMode(boolean b)
	{
	}

	public void enforceCommentTokens(TokenSet tokenSet)
	{
		enforcedCommentTokens = tokenSet;
	}

	public LighterASTNode getLatestDoneMarker()
	{
		return null;
	}

	public List<Pair<MockMarker, IElementType>> getDoneMarkers()
	{
		return doneMarkers;
	}

	public final class MockMarker implements Marker
	{
		private final int position;
		private final String details;
		private boolean addedError = false;
		boolean closed = false;

		private MockMarker(int originalPosition, String details)
		{
			this.position = originalPosition;
			this.details = details;
		}

		public Marker precede()
		{
			MockMarker preceedingMarker = new MockMarker(this.position, "preceded marker " + this);
			//fixme fix the stack of markers in the psi builder
			int pos = MockPsiBuilder.this.markers.indexOf(this);
			if(pos != -1)
			{
				MockPsiBuilder.this.markers.add(pos, preceedingMarker);
			}

			return preceedingMarker;
		}

		private void finishMarker()
		{
			if(closed)
			{
				throw new IllegalStateException("This marker is already closed (either dropped or done)!");
			}

			closed = true;

			if(MockPsiBuilder.this.markers.isEmpty() || MockPsiBuilder.this.markers.peek() != this)
			{
				StringBuilder details = new StringBuilder();
				Stack<MockMarker> markers = MockPsiBuilder.this.markers;
				//output the exising markers
				for(int i = markers.size() - 1; i >= 0; --i)
				{
					final MockMarker m = markers.get(i);
					if(m == this)
					{
						details.append("## current marker follows ##");
					}
					details.append(m.details).append("\n\n");
				}

				boolean hasCurrent = MockPsiBuilder.this.markers.contains(this);
				String detailMessage = "This marker isn't closed in order. Current markers (newest is later):\n" + details.toString();
				if(!hasCurrent)
				{
					detailMessage = "++ The current marker is already closed!\n" + detailMessage;
				}

				throw new IllegalStateException(detailMessage);
			}

			MockPsiBuilder.this.markers.pop();
		}

		public void drop()
		{
			finishMarker();
		}

		public void rollbackTo()
		{
			MockPsiBuilder.this.elementPosition = position;
			finishMarker();
		}

		public void done(IElementType elementType)
		{
			finishMarker();

			doneMarkers.add(Pair.create(this, elementType));
		}

		public void collapse(IElementType iElementType)
		{
			done(iElementType);
		}

		public void doneBefore(IElementType elementType, Marker marker)
		{
			finishMarker();
			doneMarkers.add(Pair.create(this, elementType));
		}

		public void doneBefore(IElementType elementType, Marker marker, LocalizeValue s)
		{
			finishMarker();
			doneMarkers.add(Pair.create(this, elementType));
		}

		public void error(LocalizeValue s)
		{
			MockPsiBuilder.this.error("Marker@" + position + ": " + s);
			addedError = true;
			finishMarker();
		}

		public void errorBefore(LocalizeValue s, Marker marker)
		{
			//fixme
			error(s);
		}

		public void setCustomEdgeTokenBinders(@Nullable WhitespacesAndCommentsBinder whitespacesAndCommentsBinder, @Nullable WhitespacesAndCommentsBinder whitespacesAndCommentsBinder1)
		{

		}
	}
}
