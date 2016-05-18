(function(angular) {"use strict"; angular.module("angular-elastic-builder").run(["$templateCache", function($templateCache) {$templateCache.put("angular-elastic-builder/BuilderDirective.html","<div class=\"elastic-builder\">\r\n  <div class=\"filter-panels\">\r\n    <div class=\"list-group form-inline\">\r\n      <div\r\n        data-ng-repeat=\"filter in filters\"\r\n        data-elastic-builder-chooser=\"filter\"\r\n        data-elastic-fields=\"data.fields\"\r\n        data-on-remove=\"removeChild($index)\"\r\n        data-depth=\"0\"></div>\r\n      <div class=\"list-group-item actions\">\r\n        <a class=\"btn btn-xs btn-primary\" title=\"Add Rule\" data-ng-click=\"addRule()\">\r\n          <i class=\"fa fa-plus\"></i>\r\n        </a>\r\n        <a class=\"btn btn-xs btn-primary\" title=\"Add Group\" data-ng-click=\"addGroup()\">\r\n          <i class=\"fa fa-list\"></i>\r\n        </a>\r\n      </div>\r\n    </div>\r\n  </div>\r\n</div>\r\n");
$templateCache.put("angular-elastic-builder/ChooserDirective.html","<div\r\n  class=\"list-group-item elastic-builder-chooser\"\r\n  data-ng-class=\"getGroupClassName()\">\r\n\r\n  <div data-ng-if=\"item.type === \'group\'\"\r\n    data-elastic-builder-group=\"item\"\r\n    data-depth=\"{{ depth }}\"\r\n    data-elastic-fields=\"elasticFields\"\r\n    data-on-remove=\"onRemove()\"></div>\r\n\r\n  <div data-ng-if=\"item.type !== \'group\'\"\r\n    data-elastic-builder-rule=\"item\"\r\n    data-elastic-fields=\"elasticFields\"\r\n    data-on-remove=\"onRemove()\"></div>\r\n\r\n</div>\r\n");
$templateCache.put("angular-elastic-builder/GroupDirective.html","<div class=\"elastic-builder-group\">\r\n  <h7>Se\r\n    <select data-ng-model=\"group.subType\" ui-select2>\r\n      <option value=and>todas</option>\r\n      <option value=\"or\">alguma</option>\r\n    </select>\r\n    (d)as condi&ccedil;&otilde;es forem satisfeitas\r\n  </h7>\r\n  <div\r\n    data-ng-repeat=\"rule in group.rules\"\r\n    data-elastic-builder-chooser=\"rule\"\r\n    data-elastic-fields=\"elasticFields\"\r\n    data-depth=\"{{ +depth + 1 }}\"\r\n    data-on-remove=\"removeChild($index)\"></div>\r\n\r\n  <div class=\"list-group-item actions\" data-ng-class=\"getGroupClassName()\">\r\n    <a class=\"btn btn-xs btn-primary\" title=\"Adicionar regra\" data-ng-click=\"addRule()\">\r\n      <i class=\"fa fa-plus\"></i>\r\n    </a>\r\n    <a class=\"btn btn-xs btn-primary\" title=\"Adicionar grupo\" data-ng-click=\"addGroup()\">\r\n      <i class=\"fa fa-list\"></i>\r\n    </a>\r\n    <a class=\"btn btn-xs btn-danger remover\" data-ng-click=\"onRemove()\">\r\n      <i class=\"fa fa-minus\"></i>\r\n    </a>\r\n  </div>\r\n\r\n</div>\r\n");
$templateCache.put("angular-elastic-builder/RuleDirective.html","<div class=\"elastic-builder-rule\">\r\n  <select data-ui-select2=\"uiSelectOptions\" data-ng-model=\"rule.field\" data-ng-change=\"initRule()\">\r\n  	<option data-ng-repeat=\"(key, value) in elasticFields\" value=\"{{ key }}\">{{ value.description }}</option>\r\n  </select>\r\n\r\n  <span data-elastic-type=\"getType()\" data-rule=\"rule\" data-guide=\"elasticFields[rule.field]\"></span>\r\n\r\n  <a class=\"btn btn-xs btn-danger remover\" data-ng-click=\"onRemove()\">\r\n    <i class=\"fa fa-minus\"></i>\r\n  </a>\r\n\r\n</div>\r\n");
$templateCache.put("angular-elastic-builder/types/Boolean.html","<span class=\"boolean-rule\">\r\n  <select data-ng-model=\"rule.subType\" data-ui-select2=\"uiSelectOptions\">\r\n    <!-- Term Options -->\r\n    <optgroup label=\"Texto\">\r\n      <option value=\"equals\">Igual</option>\r\n    </optgroup>\r\n\r\n    <!-- Generic Options -->\r\n    <optgroup label=\"Gen&eacute;rico\">\r\n      <option value=\"exists\">Existe</option>\r\n      <option value=\"notExists\">N&atilde;o existe</option>\r\n    </optgroup>\r\n  </select>\r\n\r\n  <select data-ui-select2=\"uiSelectOptions\" data-ng-if=\"inputNeeded()\" data-ng-model=\"rule.value\">\r\n	<option data-ng-repeat=\"choice in booleans\" value=\"{{ choice.value }}\">{{ choice.description }}</option>\r\n  </select>\r\n</span>\r\n");
$templateCache.put("angular-elastic-builder/types/Date.html","<span class=\"date-rule\">\r\n  <select data-ng-model=\"rule.subType\" data-ui-select2=\"uiSelectOptions\">\r\n\r\n    <optgroup label=\"Per&iacute;odo\">\r\n      <option value=\"last\">Nos &uacute;ltimos</option>\r\n      <option value=\"next\">Nos pr&oacute;ximos</option>\r\n    </optgroup>\r\n\r\n    <optgroup label=\"Data\">\r\n      <option value=\"equals\">=</option>\r\n      <option value=\"gt\">&gt;</option>\r\n      <option value=\"gte\">&ge;</option>\r\n      <option value=\"lt\">&lt;</option>\r\n      <option value=\"lte\">&le;</option>\r\n    </optgroup>\r\n\r\n    <optgroup label=\"Gen&eacute;rico\">\r\n      <option value=\"exists\">Existe</option>\r\n      <option value=\"notExists\">N&atilde;o existe</option>\r\n    </optgroup>\r\n  </select>\r\n\r\n  <span data-ng-if=\"numberNeeded()\"><input type=\"number\" class=\"form-control\"\r\n    data-ng-model=\"rule.value\" min=0 size=\"3\" /> dias</span>\r\n\r\n  <input type=\"text\" class=\"form-control\" data-ng-if=\"dateNeeded()\" data-provide=\"datepicker\" data-date-format=\"dd/mm/yyyy\"\r\n    data-ng-model=\"rule.date\" data-ng-init=\"init(rule.date,\'\')\" />\r\n\r\n</span>\r\n");
$templateCache.put("angular-elastic-builder/types/Multi.html","<span class=\"multi-rule\">\r\n  <select data-ng-model=\"rule.subType\" data-ui-select2=\"uiSelectOptions\">\r\n  \r\n    <optgroup label=\"Texto\">\r\n      <option value=\"equals\">Igual</option>\r\n      <option value=\"notEquals\">Diferente</option>\r\n    </optgroup>\r\n    \r\n    <optgroup label=\"Gen&eacute;rico\">\r\n      <option value=\"exists\">Existe</option>\r\n      <option value=\"notExists\">N&atilde;o existe</option>\r\n    </optgroup>\r\n  </select>\r\n  <select multiple=\"multiple\" data-ui-select2=\"uiSelectOptions\"\r\n  	data-ng-multiple=\"true\" data-ng-if=\"inputNeeded()\" data-ng-model=\"rule.value\">\r\n	<option data-ng-repeat=\"choice in guide.choices\" value=\"{{ choice.value }}\">{{ choice.description }}</option>\r\n  </select>\r\n</span>\r\n");
$templateCache.put("angular-elastic-builder/types/Number.html","<span class=\"number-rule\">\r\n  <select data-ng-model=\"rule.subType\" data-ui-select2=\"uiSelectOptions\">\r\n    <optgroup label=\"Numeral\">\r\n      <option value=\"equals\">=</option>\r\n      <option value=\"notEquals\">!=</option>\r\n      <option value=\"gt\">&gt;</option>\r\n      <option value=\"gte\">&ge;</option>\r\n      <option value=\"lt\">&lt;</option>\r\n      <option value=\"lte\">&le;</option>\r\n    </optgroup>\r\n\r\n    <optgroup label=\"Gen&eacute;rico\">\r\n      <option value=\"exists\">Existe</option>\r\n      <option value=\"notExists\">N&atilde;o existe</option>\r\n    </optgroup>\r\n  </select>\r\n\r\n  <!-- Range Fields -->\r\n  <input type=\"number\" class=\"form-control\" data-ng-if=\"inputNeeded()\"\r\n    data-ng-model=\"rule.value\" min=\"{{ guide.minimum }}\" max=\"{{ guide.maximum }}\" />\r\n</span>\r\n");
$templateCache.put("angular-elastic-builder/types/Term.html","<span class=\"elastic-term\">\r\n  <select data-ng-model=\"rule.subType\" data-ui-select2=\"uiSelectOptions\">\r\n    <!-- Term Options -->\r\n    <optgroup label=\"Texto\">\r\n      <option value=\"equals\">Igual</option>\r\n      <option value=\"notEquals\">Diferente</option>\r\n      <option value=\"prefix\">Come&ccedil;a</option>\r\n      <option value=\"contains\">Cont&eacute;m</option>\r\n    </optgroup>\r\n\r\n    <!-- Generic Options -->\r\n    <optgroup label=\"Gen&eacute;rico\">\r\n      <option value=\"exists\">Existe</option>\r\n      <option value=\"notExists\">N&atilde;o existe</option>\r\n    </optgroup>\r\n\r\n  </select>\r\n  <input class=\"form-control\" type=\"text\" data-ng-if=\"inputNeeded()\"\r\n   data-ng-model=\"rule.value\" />\r\n</span>\r\n");}]);})(window.angular);