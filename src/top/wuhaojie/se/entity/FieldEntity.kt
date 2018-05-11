package top.wuhaojie.se.entity

import org.jdesktop.swingx.ux.CellProvider
import org.jdesktop.swingx.ux.Selector

data class FieldEntity(

        var source: String = "",
        var result: String = "",
        var isSelected: Boolean = true,
        var resultSrc: String = ""

) : Selector, CellProvider {


    override fun setSelect(select: Boolean) {
        this.isSelected = select
    }


    override fun getCellTitle(index: Int): String {
        return when (index) {
            0 -> source
            1 -> result
            else -> ""
        }
    }


    override fun setValueAt(column: Int, text: String?) {
        when (column) {
            0 -> source = text ?: ""
            1 -> result = text ?: ""
        }
    }


}