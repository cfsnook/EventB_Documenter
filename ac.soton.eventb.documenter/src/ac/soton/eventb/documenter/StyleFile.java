/*******************************************************************************
 * Copyright (c) 2017-2018 University of Southampton.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     University of Southampton - initial API and implementation
 *******************************************************************************/
package ac.soton.eventb.documenter;

import java.io.InputStream;

import org.apache.tools.ant.filters.StringInputStream;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
/**
 * <p>
 * generates lstEventB style file.
 * </p>
 * @author dd4g12
 * @version 0.0.1
 * @since 0.0.1
 */
public class StyleFile {
	final static String fileName = "lstEventB.sty";
	
	public static String setContent(){
		String str = "";
		str += "%%\n"+
		"%% This is file `lstEventB.sty',\n" +
		"%% generated with the docstrip utility. \n" +
		"%%\n" +
		"%% The original source files were:\n" +
		"%%\n" +
		"%% lstEventB.dtx  (with options: `lstEventB') \n" +
		"%% \n" +
		"%% This is a generated file.\n" +
		"%% \n" +
		"%% Copyright (C) 2017 by Thai Son Hoang and Chenyang Zhu \n" +
		"%% <T dot S dot Hoang and C dot Zhu at ecs dot soton dot ac dot uk> \n" +
		"%%  \n" +
		"%% This file may be distributed and/or modified under the \n" +
		"%% conditions of the LaTeX Project Public License, either version 1.3 \n" +
		"%% of this license or (at your option) any later version. \n" +
		"%% The latest version of this license is in: \n" +
		"%%  \n" +
		"%%    http://www.latex-project.org/lppl.txt \n" +
		"%% \n" +
		"%% and version 1.3 or later is part of all distributions of LaTeX \n" +
		"%% version 2005/12/01 or later. \n" +
		"%%  \n" +
		"%% This work has the LPPL maintenance status \"author-maintained\". \n" +
		"%%  \n" +
		"%% The Current Maintainer of this work is T. S. Hoang and C. Zhu. \n" +
		"%%  \n" +
		"%% This file was modified by D. Dghaym to work with iUML-B Document Generator. \n" +
		"%%  \n" +
		
		"\\NeedsTeXFormat{LaTeX2e}\\relax  \n" +
		"\\ProvidesPackage{lstEventB} \n" +
		    "[2017/08/10 v0.1 Package for listing Event-B code] \n" +

		"%%%%% BEGIN Package loading %%%%% \n" +
		"\\RequirePackage{listings} \n" +
		"\\RequirePackage{xspace} \n" +
		"\\RequirePackage{xcolor} \n" +
		"\\RequirePackage{bsymb} \n" +
		"\\RequirePackage{xargs} \n" +
		"%%%%% END Package loading %%%%% \n" +

		"%%%%% BEGIN Declaration of options %%%%% \n" +
		"% ========================  \n" +

		"% Macro to set the colour of the Event-B keywords. \n" +
		"% The default colour is black. \n" +
		"\\newcommand{\\EventB@SetKeywordColour}[1]{% \n" +
		  "\\colorlet{EventB@keywordcolour}{#1}% \n" +
		"} \n" +
		"\\EventB@SetKeywordColour{black} \n" +

		"% Macro to set the colour of the Event-B secondary keywords. \n" +
		"% The default colour is black. \n" +
		"\\newcommand{\\EventB@SetNdKeywordColour}[1]{% \n" +
		  "\\colorlet{EventB@ndkeywordcolour}{#1}% \n" +
		"} \n" +
		"\\EventB@SetNdKeywordColour{black} \n" +

		"% Macro to set the colour of Event-B identifiers. \n" +
		"% The default colour is black. \n" +
		"\\newcommand{\\EventB@SetIdentifierColour}[1]{% \n" +
		  "\\colorlet{EventB@identifiercolour}{#1}% \n" +
		"} \n" +
		"\\EventB@SetIdentifierColour{black} \n" +

		"% Macro to set the colour of Event-B comments. \n" +
		"% The default colour is black. \n" +
		"\\newcommand{\\EventB@SetCommentColour}[1]{% \n" +
		  "\\colorlet{EventB@commentcolour}{#1}% \n" +
		"} \n" +
		"\\EventB@SetCommentColour{black}\n" +

		"% Macro to set the colour of Event-B formulae. \n" +
		"% The default colour is black. \n" +
		"\\newcommand{\\EventB@SetFormulaColour}[1]{% \n" +
		  "\\colorlet{EventB@formulacolour}{#1}% \n" +
		"} \n" +
		"\\EventB@SetFormulaColour{black} \n" +

		"% Declaration of the *colour* option. \n" +
		"\\DeclareOption{colour}{ \n" +
		  "\\EventB@SetKeywordColour{red} \n" +
		  "\\EventB@SetNdKeywordColour{red} \n" +
		  "\\EventB@SetIdentifierColour{purple} \n" +
		  "\\EventB@SetCommentColour{green} \n" +
		 "\\EventB@SetFormulaColour{blue} \n" +
		"} \n" +

		"% Declaration of the *color* option as an alias of *colour*. \n" +
		"\\DeclareOption{color}{ \n" +
		  "\\ExecuteOptions{colour} \n" +
		"} \n" +
		"%%%%% END Declaration of options %%%%% \n" +

		"%%%%% BEGIN Execution of options %%%%% \n" +
		"% ======================== \n" +

		"\\ProcessOptions \n" +
		"%%%%% END Execution of options %%%%% \n" +
		"%%%%% BEGIN Typesetting of the Event-B Language %%%%% \n" +
		"% =================================== \n" +

		"% Defining the Event-B language \n" +
		"\\lstdefinelanguage{Event-B}{% \n" +
		  "basicstyle=\\rmfamily\\footnotesize, \n" +
		  "keywords={% \n" +
		   " % Keywords for contexts \n" +
		    "context,extends,sets,constants,axioms,theorem,end,% \n" +
		    "% Keywords for machines \n" +
		   " machine,sees,refines,variables,invariants,variant,events,% \n" +
		 " },% \n" +
		  "keywordstyle=\\color{EventB@keywordcolour}\\bf\\sffamily,% \n" +
		 " sensitive=false, \n" +
		  "ndkeywords={% \n" +
		    "% Keywords for events \n" +
		    "extended,theorem,any,where,when,with,begin,then% \n" +
		  "},% \n" +
		  "ndkeywordstyle=\\color{EventB@ndkeywordcolour}\\bf\\sffamily,% \n" +
		  "identifierstyle=\\color{EventB@identifiercolour}\\sffamily, \n" +
		  "comment=[l]{//},% \n" +
		  "morecomment=[s]{/*}{*/},% \n" +
		  "commentstyle=\\color{EventB@commentcolour}\\rmfamily,% \n" +
		  "stringstyle=\\color{EventB@formulacolour}\\sffamily, \n" +
		  "string=[b]\", \n" +
		  "showstringspaces=false, % Do not show the space in formulae \n" +
		  "inputencoding=utf8, % Allow UTF-8 input encoding \n" +
		  "extendedchars=true, % Use extended characters \n" +
		  "literate= % Event-B mathematical symbols \n" +
		  "{‚ä•}{{$\\bfalse$}}1% \n" +
		  "{‚ä§}{{$\\btrue$}}1% \n" +
		  "{‚àß}{{$\\land$}}1% \n" +
		  "{‚à®}{{$\\lor$}}1% \n" +
		  "{‚áí}{{$\\limp$}}1% \n" +
		  "{‚áî}{{$\\leqv$}}1% \n" +
		  "{¬¨}{{$\\lnot$}}1% \n" +
		  "{‚àÄ}{{$\\forall$}}1% \n" +
		  "{‚àÉ}{{$\\exists$}}1% \n" +
		  "{¬∑}{{$\\qdot$}}1% \n" +
		  "{‚â†}{{$\\neq$}}1% \n" +
		  "{‚àÖ}{{$\\emptyset$}}1% \n" +
		  "{‚à™}{{$\\bunion$}}1% \n" +
		  "{‚à©}{{$\\binter$}}1% \n" +
		  "{‚àñ}{{$\\setminus$}}1% \n" +
		  "{‚Ü¶}{{$\\mapsto$ }}1% \n" +
		  "{√ó}{{$\\cprod$ }}1% \n" +
		  "{‚Ñô}{{$\\pow$ }}1% \n" +
		  "{‚Ñô1}{{$\\pown$ }}1% \n" +
		  "{‚àà}{{$\\in$}{ }}2% \n" +
		  "{‚àâ}{{$\\notin$ }}1% \n" +
		  "{‚äÜ}{{$\\subseteq$}}1% \n" +
		  "{‚äà}{{$\\nsubseteq$ }}1% \n" +
		  "{‚äÇ}{{$\\subset$ }}1% \n" +
		  "{‚äÑ}{{$\\nsubset$ }}1% \n" +
		  "{‚Ñ§}{{$\\intg$ }}1% \n" +
		  "{‚Ñï}{{$\\nat$}}1% \n" +
		  "{‚Ñï1}{{$\\natn$ }}1% \n" +
		  "{‚â•}{{$\\geq$ }}1% \n" +
		  "{‚â§}{{$\\leq$ }}1% \n" +
		  "{‚Üî}{{$\\rel$ }}1% \n" +
		  "{‚àò}{{$\\circ$ }}1% \n" +
		  "{‚óÅ}{{$\\domres$ }}1% \n" +
		  "{‚©§}{{$\\domsub$}}1% \n" +
		  "{‚ñ∑}{{$\\ranres$ }}1% \n" +
		  "{‚©•}{{$\\ransub$ }}1% \n" +
		  "{‚àº}{{$\\sim$}}1% \n" +
		  "{ÓÑÉ}{{$\\ovl$ }}1% \n" +
		  "{‚äó}{{$\\dprod$ }}1% \n" +
		  "{‚à•}{{$\\pprod$ }}1% \n" +
		  "{‚á∏}{{$\\pfun$ }}1% \n" +
		  "{‚Üí}{{$\\tfun$}}1% \n" +
		  "{‚§î}{{$\\pinj$ }}1% \n" +
		  "{‚Ü£}{{$\\tinj$ }}1% \n" +
		  "{‚§Ä}{{$\\psur$ }}1% \n" +
		  "{‚Ü†}{{$\\tsur$ }}1% \n" +
		  "{‚§ñ}{{$\\tbij$ }}1% \n" +
		  "{Œª}{{$\\lambda$ }}1% \n" +
		  "{‚âî}{{$\\bcmeq$}{ }}2% \n" +
		  "{:‚àà}{{$\\bcmin$}{ }}2% \n" +
		  "{:‚à£}{{$\\bcmsuch$}{ }}2% \n" +
		  ", % End of Event-B mathematical symbols \n" +
		"} \n" +

		"% Type setting inline Event-B code using | \n" +
		"\\lstMakeShortInline[language=Event-B, breaklines=f, basicstyle=\\rmfamily\\normalsize]| \n" +

		"% Multi-line Event-B code should be wrapped in EventBcode environment. \n" +
		"\\lstnewenvironment{EventBcode}{\\lstset{language=Event-B}}{} \n" +
		"\\lstset{% \n" +
		  "columns=fullflexible, % The columns are fully flexible. \n" +
		  "numberbychapter=false, \n" +
		  "%frame=top,frame=bottom, % There are line (frame at top and bottom). \n" +
		  "stepnumber=1, % the step between two line-numbers. If it is 1 each line will be numbered \n" +
		  "%numberstyle=\\tiny, \n" +
		 "% numbersep=5pt, % how far the line-numbers are from the code \n" +
		  "tabsize=2, % tab size in blank spaces \n" +
		  "breaklines=true, % sets automatic line breaking \n" +
		  "captionpos=b, % sets the caption-position to top \n" +
		  "mathescape=false, \n" +
		  "showspaces=false, % Do not show spaces \n" +
		  "showtabs=false, % Do not show tabs \n" +
		  "xleftmargin=10pt, \n" +
		 "% framexleftmargin=10pt, \n" +
		 "% framexrightmargin=0pt, \n" +
		 "% framexbottommargin=5pt, \n" +
		 "% framextopmargin=5pt, \n" +
		  "escapechar=\\%, \n" +
		  "numbers=none, % where to put the line-numbers; possible values are (none, left, right) \n" +
		 " %numbersep=5pt, \n" +
		"} \n" +
		"\\newcommandx{\\EventBinputlisting}[2][1=]{% \n" +
		  "\\begin{mdframed}[backgroundcolor=yellow!10, rightline=false,leftline=false] \n" +
		    "\\lstinputlisting[language=Event-B,mathescape,frame={},#1]{#2} \n" +
		  "\\end{mdframed} \n" +
		"} \n" +
		"%%%%% END Typesetting of the Event-B Language %%%%% \n" +
		"\\newcommand{\\eventB}{} \n" +
		"\\endinput \n" +
		"%% \n" +
		"%% End of file `lstEventB.sty'. \n" ;

	 return str;
	}
	
	public static void createStyleFile(IProject proj){
	//----------------------
		
		IFolder folder = proj.getFolder("EventB_Documents");
		

			IFile file = folder.getFile(fileName);
			//set document content
		    String content = setContent();
			InputStream input = new StringInputStream(content);
			
		    try {
		    	file.create(input, IResource.FORCE, null);
			} catch (CoreException e) {
				throw new RuntimeException("Could not create " + fileName, e);
			}
			
    //--------------------
	}
	
}
