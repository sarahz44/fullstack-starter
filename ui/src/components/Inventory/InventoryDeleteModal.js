import Button from '@material-ui/core/Button'
import Dialog from '@material-ui/core/Dialog'
import DialogActions from '@material-ui/core/DialogActions'
import DialogContent from '@material-ui/core/DialogContent'
import DialogTitle from '@material-ui/core/DialogTitle'
import Grid from '@material-ui/core/Grid'
import React from 'react'
import Typography from '@material-ui/core/Typography'
import { Form, Formik } from 'formik'

class InventoryDeleteModal extends React.Component {
    render() {
       const {
        handleDialog,
        handleDelete,
        isDialogOpen,
        initialValues,
       } = this.props
    

    return (
        <Dialog
            open={isDialogOpen}
            onClose={() => handleDialog(false)}
        >

            
        </Dialog>
    )
   }    
}

export default InventoryDeleteModal