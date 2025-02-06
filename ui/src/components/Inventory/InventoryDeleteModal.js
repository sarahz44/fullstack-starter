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
      initialValues,
      isDialogOpen,
    } = this.props
    // Problem: checked values are not added to selected array
    return (
      <Dialog
        open={isDialogOpen}
        onClose={() => handleDialog(false)}
      >
        {console.warn(initialValues)}
        <Formik
          initialValues={initialValues}
          onSubmit={ values => {
            console.warn('THE VALUE' + values)
            handleDelete(values)
            handleDialog(values)
          }}>
          { helpers =>
            <Form
              autoComplete='off'
              id={'deleteInventory'}
            >
              <DialogTitle id='alert-dialog-title'>
                Delete Inventory
              </DialogTitle>
              <DialogContent>
                <Grid container>
                  <Grid xs={12}>
                    <Typography>
                       Are you sure you want to delete this inventory
                    </Typography>
                  </Grid>
                </Grid>
              </DialogContent>
              <DialogActions>
                <Button onClick={() => { handleDialog(false) }} color='secondary'>No</Button>
                <Button disableElevation variant='contained' type='submit' form={'deleteInventory'} color='secondary'>
                  Yes
                </Button>
              </DialogActions>
            </Form>
          }

        </Formik>
      </Dialog>
    )
  }
}

InventoryDeleteModal.defaultProps = {
  delete: {}
}


export default InventoryDeleteModal