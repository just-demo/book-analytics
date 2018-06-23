<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Book parser</title>
<script type="text/javascript" src="jquery-1.6.4.js"></script>
<script type="text/javascript" src="jquery-ui.min.js"></script>
<link rel="stylesheet" type="text/css" href="style.css"></link>
<script type="text/javascript">
var ttt = [[["the","",3407,0,1,1,["","bla bla bla... some translation"]]]];
$(document).ready(function(){
	//postUpload(ttt);
});
	
function postUpload(data) {
	var start = new Date();
	persistentData = data;
	$("#book_uploader").hide();
	buildWords(data);
	buildFilter();
	logTime(start);
}

function logTime(start) {
	var time = (((new Date()).getTime() - start.getTime()) / 1000);
	//alert(time);
}

function buildWords(data) {
	window.data = data;
	var words = [];
	var i = 0;
	for ( var j in data) {
		var wordInfos = data[j];
		words.push('<div class="group">');
		words.push('<div class="groupWrapper">');
		var totalHint = [];
		var total = 0;
		for (var k in wordInfos) {
			var wordInfo = wordInfos[k];
			total += wordInfo[2];
			totalHint.push(wordInfo[0] + ": " + wordInfo[2]);
		}
		totalHint = totalHint.join(", ");
		for (var k in wordInfos) {
			++i;
			var wordInfo = wordInfos[k];
			var classes = [getNameClass(wordInfo)];
			if ( wordInfo[1]) {classes.push("translated");}
			if ( wordInfo[3]) {classes.push("saved");}
			if ( wordInfo[4]) {classes.push("ignorable");}
			if (!wordInfo[5]) {classes.push("unknown");}
			if (!wordInfo[3] && !wordInfo[4]) {classes.push("not_ignorable_saved");}
			if (!wordInfo[1] &&  wordInfo[5]) {classes.push("not_translated_unknown");}
			
			classes = classes.join(" ");
			words.push(
				'<div id="', i,'" data-groupindex="',  j, '" data-wordindex="',  k, '" class="word ', classes, '">',
					'<span class="number">', parseInt(i) + 1, '</span>',
					'<span><input id="text_',  i, '" data-id="', i, '" type="text" name="text" value="', encode(wordInfo[0]), '"/></span>',
					'<span><input id="translation_',  i, '" data-id="', i, '" type="text" name="translation" value="', encode(wordInfo[1]), '"/><button data-id="', i, '" type="button" class="translate">?</button></span>',
					'<span class="number">', wordInfo[2], '</span>',
					'<span title="', totalHint, '" class="number">', total, '</span>',
					'<span>',
						'<input type="checkbox" data-id="', i, '" name="ignore" ', (wordInfo[4] ? ' checked="checked"' : '') , '/>',
					'</span>',
				'</div>'
			);
		}
		words.push('</div>');
		words.push('</div>');
	}
	
	function encode(text) {
		return text ? text.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;') : text;
	}

	function getNameClass(wordInfo) {
		function isLowerCase(string) {
			return string.toLowerCase() == string;
		}
		if (wordInfo[1]) {
			if (isLowerCase(wordInfo[0].charAt(0))) {
				if (isLowerCase(wordInfo[1].charAt(0))) {
					return "common";
				}
			}
			else if (!isLowerCase(wordInfo[1].charAt(0))) {
				return "proper";
			}
		}
		return "undefined";
	}

	$("#words").html(words.join(""));
	$("input:text", "#words").keypress(function(e) {
	    if(e.keyCode == 13) {
	    	setTranslation($(this).data("id"));
	    }
	    else{
	    	$("#" + $(this).data("id")).addClass("modified");
	    }
	});
	
	$("input:checkbox[name='ignore']", "#words").change(function() {
		setIgnorable($(this).data("id"), $(this).is(':checked'));
	});

	$(".translate", "#words").click(function(e) {
		var id = $(this).data("id");
		var wordRow = $("#" + id);
		var translationMenu = $("#translationMenu");
		if (!translationMenu.length) {
			translationMenu = $([
				'<table id="translationMenu">',
					'<tr>',
						'<td valign="top"><div id="translationMenuItems"></div></td>',
						'<td valign="top"><div id="translationMenuClose">x</div></td>',
					'</tr>',
				'</table>',
			].join(""));
			$("body").append(translationMenu);
			$("#translationMenuClose").click(hideTranslationMenu);
		}

		var translations = getTranslations(wordRow);
		var translationItems = [];
		for (var i = 0; i < translations.length; ++i) {
			if (translations[i]) {
				translationItems.push('<div>', translations[i], '</div>');
			}
		}
		translationItems = translationItems.length ? translationItems.join("") : "o_O";
		var translationMenuItems = $("#translationMenuItems").empty().html(translationItems);
		$("div", translationMenuItems).click(function(){
			$("input[name='translation']", wordRow).val($(this).text());
			wordRow.addClass("modified");
			hideTranslationMenu();
		});
	    
		var wordRowPosition = wordRow.position();
	    translationMenu.css({
	        top: wordRowPosition.top + "px",
	        left: (wordRowPosition.left + wordRow.outerWidth()) + "px"
	    }).show();

		function getTranslations (wordRow) {
			var translations = data[wordRow.data("groupindex")][wordRow.data("wordindex")][6];
			return translations || [];
		}
	});

	function translate(id) {
		var wordRow = $("#" + id);
		var text = $.trim($("input[name='text']", wordRow).val());
		$.ajax({
			async : true,
			type : "GET",
			url : 'rest/word/translate',
			data : {"text" : text},
			success : function(data) {
				var translation = data && data[0] ? data[0] : "";
				$("input[name='translation']", wordRow).val(translation);
			},
			error : errorHandler
		});
	}

	function setTranslation(id) {
		var wordRow = $("#" + id);
		var text = $.trim($("input[name='text']", wordRow).val());
		var translation = $.trim($("input[name='translation']", wordRow).val());
		$.ajax({
			async : false,
			type : "POST",
			url : 'rest/word/translate',
			data : {
				"text" : text,
				"translation" : translation
			},
			success : function() {
				var wordData = data[wordRow.data("groupindex")][wordRow.data("wordindex")];
				wordData[0] = text;
				wordData[1] = translation;
				wordRow.removeClass("modified");
				applyClass(wordRow, "translated saved");
			},
			error : errorHandler
		});
	}

	function applyClass(wordRow, clazz) {
		wordRow.addClass(clazz);
		clazz = clazz.split(" ");
		for (var i in clazz) {
			var type = clazz[i];
			if (type && !$("#filter_" + type).is(":checked")) {
				wordRow.hide();
				hideTranslationMenu();
				break;
			}
		}
	}
	
	function errorHandler(data) {
		alert("An error occured!");
	}

	function setIgnorable(id, ignorable) {
		var wordRow = $("#" + id);
		var text = $.trim($("input[name='text']", wordRow).val());
		$.ajax({
			async : false,
			type : "POST",
			url : 'rest/word/ignore',
			data : {
				"text" : text,
				"ignorable" : ignorable
			},
			success : function() {
				if (ignorable) {
					applyClass(wordRow, "ignorable");
				} else {
					wordRow.removeClass("ignorable");
				}
			},
			error : errorHandler
		});
	}
}

function hideTranslationMenu(){
	$("#translationMenu").hide();
}

function buildFilter() {
	var hidden = {};
	var types = [ {"common"   : "Common"   , "proper"     : "Proper"    , "undefined"             : "Undefined"},
	              {"unknown"  : "Unknown"  , "translated" : "Translated", "not_translated_unknown": "Other"},
				  {"ignorable": "Ignorable", "saved"      : "Saved"     , "not_ignorable_saved"   : "Other"}
				];
	var filter = [];
	filter.push("<table>");
	for ( var i in types) {
		filter.push("<tr>");
		for ( var type in types[i]) {
			var typeLabel = types[i][type];
			if (typeLabel == null) {
				filter.push("<td> </td>");
			} else {
				filter.push(
					"<td class='", type ,"'>",
						"<input id='filter_", type, "' type='checkbox' checked='checked' value='", type, "'/>",
						"<label for='filter_", type, "'>", typeLabel, "</label>",
					"</td>"
				);
			}
		}
		filter.push("</tr>");
	}
	filter.push("</table>");

	$("#filter").html(filter.join("")).show().draggable();

	$("#words").css({marginTop: (10 + $("#filter").outerHeight()) + "px"});
	
	$("input", "#filter").change(function() {
		var type = $(this).val();
		if ($(this).is(':checked')) {
			$(".word." + type).each(function() {
				delete hidden[type];
				var wordRow = $(this);
				for ( var hiddenClass in hidden) {
					if (wordRow.hasClass(hiddenClass)) {
						return;
					}
				}
				wordRow.show();
			});
		} else {
			hidden[type] = type;
			$(".word." + type).hide();
			hideTranslationMenu();
		}
	});
}
</script>
</head>
<body>
	<iframe id="book_uploader" src="rest/book"></iframe>
	<div id="filter"></div>
	<div id="words"></div>
	<br/>
	<a href="vocabulary/index.html" target="_blank">Practice</a>
	<br/>
	<a href="" target="_blank">New Book</a>
	<br/>
	<a href="textToBook.jsp" target="_blank">Text To Book</a><br/>
</body>
</html>
